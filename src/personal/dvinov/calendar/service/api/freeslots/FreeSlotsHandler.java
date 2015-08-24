package personal.dvinov.calendar.service.api.freeslots;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class FreeSlotsHandler implements RequestHandler<FreeSlotsRequest, FreeSlotsResponse> {

    @Override
    public FreeSlotsResponse handleRequest(FreeSlotsRequest input, Context context) {
        context.getLogger().log("Input: " + input);
        context.getLogger().log("Context: " + context);
        
        return new FreeSlotsResponse();
    }

}
