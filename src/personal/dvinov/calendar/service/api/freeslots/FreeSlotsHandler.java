package personal.dvinov.calendar.service.api.freeslots;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import personal.dvinov.calendar.service.api.freeslots.FreeSlotsResponse.Slots;

public class FreeSlotsHandler implements RequestHandler<FreeSlotsRequest, FreeSlotsResponse> {

    @Override
    public FreeSlotsResponse handleRequest(FreeSlotsRequest input, Context context) {
        context.getLogger().log("Input: " + input);
        context.getLogger().log("Context: " + context);
        
        Day firstAvailableDay = new Day(2015, 8, 23);
        Slots slotsOnFirstDay = new Slots(ImmutableList.of(1, 2, 3));
        
        Day secondAvailableDay = new Day(2015, 8, 24);
        Slots slotsOnSecondDay = new Slots(ImmutableList.of(4, 5, 6));
        
        return new FreeSlotsResponse(ImmutableMap.of(
                firstAvailableDay, slotsOnFirstDay,
                secondAvailableDay, slotsOnSecondDay
        ));
    }

}
