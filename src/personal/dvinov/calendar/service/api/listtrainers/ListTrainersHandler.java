package personal.dvinov.calendar.service.api.listtrainers;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import personal.dvinov.calendar.service.core.trainers.dao.Trainer;
import personal.dvinov.calendar.service.core.trainers.dao.TrainerLoader;

public class ListTrainersHandler implements RequestHandler<ListTrainersRequest, ListTrainersResponse> {
    private final TrainerLoader trainerLoader;
    
    public ListTrainersHandler() {
        this.trainerLoader = new TrainerLoader(new DynamoDBMapper(new AmazonDynamoDBClient()));
    }
    
    public ListTrainersHandler(final TrainerLoader trainerLoader) {
        this.trainerLoader = trainerLoader;
    }

    @Override
    public ListTrainersResponse handleRequest(ListTrainersRequest input, Context context) {
        context.getLogger().log("Input: " + input);
        
        Validate.notNull(input.getLocation(), "Validation exception: 'location' parameter is required");
        
        final List<Trainer> loadedTrainers = trainerLoader.listActiveTrainers(input.getLocation());
        final List<ListTrainersResponse.Trainer> trainersToReturn = loadedTrainers.stream()
                .map(trainer -> new ListTrainersResponse.Trainer(trainer.getId(), trainer.getName()))
                .collect(Collectors.toList());
        return new ListTrainersResponse(trainersToReturn);
    }

}
