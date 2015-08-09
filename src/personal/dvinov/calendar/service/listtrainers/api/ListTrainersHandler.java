package personal.dvinov.calendar.service.listtrainers.api;

import java.util.LinkedList;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.common.collect.Lists;

import personal.dvinov.calendar.service.listtrainers.api.ListTrainersResponse.Trainer;

public class ListTrainersHandler implements RequestHandler<Void, ListTrainersResponse> {

    @Override
    public ListTrainersResponse handleRequest(Void input, Context context) {
        context.getLogger().log("Input: " + input);

        final Trainer trainer1 = new Trainer("1", "Trainer 1");
        final Trainer trainer2 = new Trainer("2", "Trainer 2");
        final List<Trainer> trainers = new LinkedList<>();
        trainers.add(trainer1);
        trainers.add(trainer2);
        
        return new ListTrainersResponse(Lists.newArrayList(
                new Trainer("1", "Trainer 1"),
                new Trainer("2", "Trainer 2"),
                new Trainer("3", "Trainer 3")
        ));
    }

}
