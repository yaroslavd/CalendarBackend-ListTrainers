package personal.dvinov.calendar.service.api.freeslots;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import personal.dvinov.calendar.service.api.common.util.ContextLogger;
import personal.dvinov.calendar.service.api.freeslots.FreeSlotsResponse.Slot;
import personal.dvinov.calendar.service.core.trainers.business.FreeSlotComputer;
import personal.dvinov.calendar.service.core.trainers.business.SlotBusinessObject;
import personal.dvinov.calendar.service.core.trainers.dao.BookedSlotAdapter;

public class FreeSlotsHandler implements RequestHandler<FreeSlotsRequest, FreeSlotsResponse> {
    
    private final FreeSlotComputer computer;
    private final Map<String, ZoneId> locationToTimeZone = ImmutableMap.of(
            "Seattle", ZoneId.of("America/Los_Angeles")
    );
    
    public FreeSlotsHandler() {
        final AmazonDynamoDB dynamoClient = new AmazonDynamoDBClient();
        final DynamoDBMapper mapper = new DynamoDBMapper(dynamoClient);
        final BookedSlotAdapter bookedSlotAdapter = new BookedSlotAdapter(mapper);
        
        this.computer = new FreeSlotComputer(bookedSlotAdapter);
    }

    public FreeSlotsHandler(FreeSlotComputer computer) {
        this.computer = computer;
    }

    @Override
    public FreeSlotsResponse handleRequest(final FreeSlotsRequest input, final Context context) {
        context.getLogger().log("Input: " + input);
        ContextLogger.logContext(context);
        
        final ZoneId zoneId = locationToTimeZone.get(input.getLocation());
        
        List<SlotBusinessObject> freeSlots = computer.computeFreeSlots(
                input.getQueryIntervalStart(), input.getQueryIntervalEnd(), zoneId, input.getTrainerId());
        
        return businessObjectsToResponse(freeSlots, zoneId);
    }
    
    private FreeSlotsResponse businessObjectsToResponse(
            final List<SlotBusinessObject> businessObjects,
            final ZoneId zoneId) {
        
        final Multimap<Day, Slot> resultMap =
                LinkedListMultimap.create(businessObjects.size());
        
        for (SlotBusinessObject bo : businessObjects) {
            resultMap.put(dayFromSlotBusinessObject(bo, zoneId), slotFromBusinessObject(bo, zoneId));
        }
        
        return new FreeSlotsResponse(resultMap.asMap());
    }
    
    private Day dayFromSlotBusinessObject(
            final SlotBusinessObject slotBusinessObject,
            final ZoneId zoneId
            ) {
        
        final LocalDateTime localStartTime =
                LocalDateTime.ofInstant(slotBusinessObject.getSlotStartTime(), zoneId);
        
        return new Day(
                localStartTime.getYear(),
                localStartTime.getMonthValue(),
                localStartTime.getDayOfMonth()
        );
    }

    private Slot slotFromBusinessObject(
            final SlotBusinessObject slotBusinessObject,
            final ZoneId zoneId
            ) {
        
        final LocalDateTime localStartTime =
                LocalDateTime.ofInstant(slotBusinessObject.getSlotStartTime(), zoneId);
        final LocalDateTime localEndTime =
                LocalDateTime.ofInstant(slotBusinessObject.getSlotEndTime(), zoneId);
        
        return new Slot(
                slotBusinessObject.getSlot(),
                localStartTime.getHour(),
                localStartTime.getMinute(),
                localEndTime.getHour(),
                localEndTime.getMinute()
        );
    }
}
