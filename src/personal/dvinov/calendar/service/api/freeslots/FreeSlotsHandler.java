package personal.dvinov.calendar.service.api.freeslots;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import personal.dvinov.calendar.service.api.common.LocationConfiguration;
import personal.dvinov.calendar.service.api.common.util.ContextLogger;
import personal.dvinov.calendar.service.api.freeslots.FreeSlotsResponse.Slot;
import personal.dvinov.calendar.service.core.trainers.business.FreeSlotComputer;
import personal.dvinov.calendar.service.core.trainers.business.SlotBusinessObject;
import personal.dvinov.calendar.service.core.trainers.dao.BookedSlotAdapter;

public class FreeSlotsHandler implements RequestHandler<FreeSlotsRequest, FreeSlotsResponse> {
    
    private final FreeSlotComputer computer;
    private final LocationConfiguration locationConfiguration;
    
    public FreeSlotsHandler() {
        final AmazonDynamoDB dynamoClient = new AmazonDynamoDBClient();
        final DynamoDBMapper mapper = new DynamoDBMapper(dynamoClient);
        final BookedSlotAdapter bookedSlotAdapter = new BookedSlotAdapter(mapper);
        
        this.computer = new FreeSlotComputer(bookedSlotAdapter);
        this.locationConfiguration = new LocationConfiguration();
    }

    public FreeSlotsHandler(
            final FreeSlotComputer computer, final LocationConfiguration locationConfiguration) {
        
        this.computer = computer;
        this.locationConfiguration = locationConfiguration;
    }

    @Override
    public FreeSlotsResponse handleRequest(final FreeSlotsRequest input, final Context context) {
        context.getLogger().log("Input: " + input);
        ContextLogger.logContext(context);
        
        final ZoneId zoneId = locationConfiguration.getZoneId(input.getLocation());
        
        List<SlotBusinessObject> freeSlots = computer.computeFreeSlots(
                input.getQueryIntervalStart(), input.getQueryIntervalEnd(), zoneId, input.getTrainerId());
        
        return businessObjectsToResponse(freeSlots, zoneId);
    }
    
    private FreeSlotsResponse businessObjectsToResponse(
            final List<SlotBusinessObject> businessObjects,
            final ZoneId zoneId) {
        
        final List<Slot> slots = businessObjects.stream()
                .map(bo -> slotFromBusinessObject(bo, zoneId))
                .collect(Collectors.toList());
        
        return new FreeSlotsResponse(slots);
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
                localStartTime.getYear(),
                localStartTime.getMonthValue(),
                localStartTime.getDayOfMonth(),slotBusinessObject.getSlot(),
                localStartTime.getHour(),
                localStartTime.getMinute(),
                localEndTime.getHour(),
                localEndTime.getMinute()
        );
    }
}
