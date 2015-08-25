package personal.dvinov.calendar.service.api.freeslots;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import personal.dvinov.calendar.service.api.common.util.ContextLogger;
import personal.dvinov.calendar.service.api.freeslots.FreeSlotsResponse.Slot;

public class FreeSlotsHandler implements RequestHandler<FreeSlotsRequest, FreeSlotsResponse> {

    @Override
    public FreeSlotsResponse handleRequest(FreeSlotsRequest input, Context context) {
        context.getLogger().log("Input: " + input);
        ContextLogger.logContext(context);
        
        Day firstAvailableDay = new Day(2015, 8, 23);
        List<Slot> slotsOnFirstDay = ImmutableList.of(
                new Slot(0, 9, 0, 10, 0),
                new Slot(1, 10, 0, 11, 0)
        );
        
        Day secondAvailableDay = new Day(2015, 8, 24);
        List<Slot> slotsOnSecondDay = ImmutableList.of(
                new Slot(2, 11, 0, 12, 0),
                new Slot(3, 13, 0, 14, 0)
        );
        
        return new FreeSlotsResponse(ImmutableMap.of(
                firstAvailableDay, slotsOnFirstDay,
                secondAvailableDay, slotsOnSecondDay
        ));
    }

}
