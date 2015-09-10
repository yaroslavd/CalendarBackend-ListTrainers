package personal.dvinov.calendar.service.core.trainers.dao;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;

import personal.dvinov.calendar.service.core.trainers.business.EligibleSlotConfiguration;
import personal.dvinov.calendar.service.core.trainers.business.SlotBusinessObject;

public class BookedSlotAdapter {
    private static final LocalTime BUSINESS_OPEN = EligibleSlotConfiguration.WEEKDAYS_9_TO_5.getBusinessOpen();

    private static final DateTimeFormatter YYYY_MM_DD_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    // Uses named capturing groups
    // https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#groupname
    private static final Pattern SLOT_FROM_DAY_PLUS_SLOT_PATTERN = Pattern.compile("(.*)-(.*)-(.*)-(?<slot>.*)");
    
    private final DynamoDBMapper mapper;
    
    public BookedSlotAdapter(final DynamoDBMapper mapper) {
        this.mapper = mapper;
    }
    
    /**
     * Fetches booked slots for that trainer in the interval provided
     * 
     * @param trainerId
     * @param startTime
     * @param endTime
     * @return list of booked slots as business objects
     */
    public List<SlotBusinessObject> listBookedSlots(
            final String trainerId,
            final Instant startTime,
            final Instant endTime,
            final ZoneId zoneId) {
        
        final List<BookedSlotDao> fromDynamo = mapper.query(
                BookedSlotDao.class, listBookedSlotsExpression(trainerId, startTime, endTime, zoneId));
        
        return fromDynamo.stream()
                .map(fromDaoToBo())
                .collect(Collectors.toList());
    }
    
    public void bookSlot(
            final String trainerId,
            final String clientId,
            final int year,
            final int month,
            final int day,
            final int slot,
            final ZoneId zoneId) throws ConditionalCheckFailedException {
        
        final String dayPlusSlot = dayPlusSlotFromComponents(year, month, day, slot);
        final LocalDateTime slotStart = LocalDateTime.of(year, month, day, slot + BUSINESS_OPEN.getHour(), 0);
        final LocalDateTime slotEnd = slotStart.plusHours(1);
        
        final BookedSlotDao toDynamo = new BookedSlotDao(
                trainerId, dayPlusSlot, clientId,
                Date.from(slotStart.atZone(zoneId).toInstant()),
                Date.from(slotEnd.atZone(zoneId).toInstant()));
        
        try {
            mapper.save(toDynamo, bookSlotExpression(clientId));
        }
        catch (ConditionalCheckFailedException e) {
            // we generally don't allow overwriting slots
            // however, we allow overwriting a slot if it's already booked by this client
            // this is ugly, we should instead use
            // http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Expressions.SpecifyingConditions.html#ConditionExpressionReference
            // unfortunately, DynamoDBMapper doesn't yet support these, and its "expected" conditions
            // are not expressive enough so here we are...
            final BookedSlotDao toLoad = new BookedSlotDao(trainerId, dayPlusSlot, null, null, null);
            final BookedSlotDao bookedSlot = mapper.load(toLoad);
            if (!bookedSlot.getClientId().equals(clientId)) {
                throw new ConditionalCheckFailedException("Slot " + dayPlusSlot + " is already booked by a different client");
            }
        }
    }
    
    private DynamoDBQueryExpression<BookedSlotDao> listBookedSlotsExpression(
            final String trainerId,
            final Instant startTime,
            final Instant endTime,
            final ZoneId zoneId) {
        
        final String startDayPlusSlot = dayFromInstant(startTime, false, zoneId);
        final String endDayPlusSlot = dayFromInstant(endTime, true, zoneId);

        return new DynamoDBQueryExpression<BookedSlotDao>()
            .withKeyConditionExpression(
                    "trainerId = :trainerId AND dayPlusSlot BETWEEN :startDayPlusSlot AND :endDayPlusSlot")
            .withFilterExpression("startTime >= :startTime AND endTime < :endTime")
            // we only care about these attributes since the caller knows the others
            .withProjectionExpression("dayPlusSlot, startTime, endTime")
            .withExpressionAttributeValues(ImmutableMap.of(
                    ":trainerId", new AttributeValue().withS(trainerId),
                    ":startDayPlusSlot", new AttributeValue().withS(startDayPlusSlot),
                    ":endDayPlusSlot", new AttributeValue().withS(endDayPlusSlot),
                    ":startTime", new AttributeValue().withS(startTime.toString()),
                    ":endTime", new AttributeValue().withS(endTime.toString())
             ));
    }
    
    private DynamoDBSaveExpression bookSlotExpression(final String clientId) {
//        final ImmutableMap<String, ExpectedAttributeValue> expected
        
        return new DynamoDBSaveExpression()
//                .withConditionalOperator(ConditionalOperator.OR)
                .withExpectedEntry("clientId", new ExpectedAttributeValue().withExists(false));
//                .withExpectedEntry("clientId",
//                        new ExpectedAttributeValue(new AttributeValue().withS(clientId)));
    }

    private String dayFromInstant(
            final Instant startTime,
            final boolean includeThroughEndOfDay,
            final ZoneId zoneId) {
        
        final LocalDate ldt = LocalDateTime.ofInstant(startTime, zoneId).toLocalDate();
        
        return YYYY_MM_DD_FORMATTER.format(
                includeThroughEndOfDay ? ldt.plusDays(1) : ldt);
    }
    
    private Function<BookedSlotDao, SlotBusinessObject> fromDaoToBo() {
        return (dao) -> new SlotBusinessObject(
                dao.getStartTime().toInstant(),
                dao.getEndTime().toInstant(),
                slotFromDayPlusSlot(dao.getDayPlusSlot()));
    }
    
    private int slotFromDayPlusSlot(final String dayPlusSlot) throws IllegalStateException {
        final Matcher matcher = SLOT_FROM_DAY_PLUS_SLOT_PATTERN.matcher(dayPlusSlot);
        if (!matcher.matches()) {
            throw new IllegalStateException("No slot found in dayPlusSlot field '" + dayPlusSlot + "'");
        }
        
        final String slotString =  matcher.group("slot");
        try {
            return Integer.parseInt(slotString);
        }
        catch (NumberFormatException e) {
            throw new IllegalStateException(
                    "Slot not parseable into int in dayPlusSlot field '" + dayPlusSlot + "'", e);
        }
    }
    
    private String dayPlusSlotFromComponents(
            final int year,
            final int month,
            final int day,
            final int slot) {

        final LocalDate ld = LocalDate.of(year, month, day);
        return YYYY_MM_DD_FORMATTER.format(ld) + "-0" + slot;
    }
}
