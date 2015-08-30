package personal.dvinov.calendar.service.core.trainers.dao;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.common.collect.ImmutableMap;

import personal.dvinov.calendar.service.core.trainers.business.SlotBusinessObject;

public class BookedSlotAdapter {
    // Uses named capturing groups
    // https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#groupname
    private static final Pattern DAY_PLUS_SLOT_PATTERN = Pattern.compile("(.*)-(.*)-(.*)-(?<slot>.*)");
    
    private final DynamoDBMapper mapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    
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
    public List<SlotBusinessObject> listBookedSlots(final String trainerId, final Instant startTime, final Instant endTime) {
        final List<BookedSlotDao> fromDynamo = mapper.query(
                BookedSlotDao.class, listBookedSlotsExpression(trainerId, startTime, endTime));
        
        return fromDynamo.stream()
                .map(fromDaoToBo())
                .collect(Collectors.toList());
    }
    
    private DynamoDBQueryExpression<BookedSlotDao> listBookedSlotsExpression(
            final String trainerId,
            final Instant startTime,
            final Instant endTime) {
        
        final String startDayPlusSlot = dayFromInstant(startTime, false);
        final String endDayPlusSlot = dayFromInstant(endTime, true);

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

    private String dayFromInstant(final Instant startTime, final boolean includeThroughEndOfDay) {
        final LocalDateTime ldt = getLocalDateTime(
                includeThroughEndOfDay ? startTime.plus(1, ChronoUnit.DAYS) : startTime);
        
        return formatter.format(ldt);
    }

    private LocalDateTime getLocalDateTime(final Instant time) {
        
        return LocalDateTime.ofInstant(time, ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS);
    }
    
    private Function<BookedSlotDao, SlotBusinessObject> fromDaoToBo() {
        return (dao) -> new SlotBusinessObject(
                dao.getStartTime().toInstant(),
                dao.getEndTime().toInstant(),
                slotFromDayPlusSlot(dao.getDayPlusSlot()));
    }
    
    private int slotFromDayPlusSlot(final String dayPlusSlot) throws IllegalStateException {
        final Matcher matcher = DAY_PLUS_SLOT_PATTERN.matcher(dayPlusSlot);
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
}
