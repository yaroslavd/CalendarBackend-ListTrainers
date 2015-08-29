package personal.dvinov.calendar.service.core.trainers.dao;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.common.collect.ImmutableMap;

public class BookedSlotAdapter {
    private final DynamoDBMapper mapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    
    public BookedSlotAdapter(final DynamoDBMapper mapper) {
        this.mapper = mapper;
    }
    
    public List<BookedSlot> listBookedSlots(final String trainerId, final Instant startTime, final Instant endTime) {
        return mapper.query(BookedSlot.class, listBookedSlotsExpression(trainerId, startTime, endTime));
    }
    
    private DynamoDBQueryExpression<BookedSlot> listBookedSlotsExpression(
            final String trainerId,
            final Instant startTime,
            final Instant endTime) {
        
        final String startDayPlusSlot = dayFromInstant(startTime, false);
        final String endDayPlusSlot = dayFromInstant(endTime, true);

        return new DynamoDBQueryExpression<BookedSlot>()
            .withKeyConditionExpression(
                    "trainerId = :trainerId AND dayPlusSlot BETWEEN :startDayPlusSlot AND :endDayPlusSlot")
            .withFilterExpression("startTime >= :startTime AND endTime < :endTime")
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
}
