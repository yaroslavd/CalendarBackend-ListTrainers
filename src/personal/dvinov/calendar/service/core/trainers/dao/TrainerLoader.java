package personal.dvinov.calendar.service.core.trainers.dao;

import java.util.List;

import org.apache.commons.lang3.Validate;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.common.collect.ImmutableMap;

public class TrainerLoader {
    private final DynamoDBMapper mapper;
    
    public TrainerLoader(final DynamoDBMapper mapper) {
        this.mapper = mapper;
    }
    
    /**
     * Lists active trainers in given location
     * 
     * @param location
     * @return list of active trainers in given location; only trainer ids and names are filled in
     */
    public List<TrainerDao> listActiveTrainers(final String location) {
        // Since this table is expected to be small, we are not creating an index
        // However, if it grows large we can create a GSI with hash key of "location"
        // and range key of "active"
        return mapper.scan(TrainerDao.class, activeTrainersExpression(location));
    }
    
    private DynamoDBScanExpression activeTrainersExpression(final String location) {
        Validate.notNull(location);
        
        return new DynamoDBScanExpression()
                // we only need trainer's id and name since caller already has location and we only return active trainers
                .withProjectionExpression("id, #name")
                .withFilterExpression("active = :active AND #location = :location")
                .withExpressionAttributeNames(ImmutableMap.of(
                        "#name", "name",
                        "#location", "location"))
                .withExpressionAttributeValues(ImmutableMap.of(
                        ":active", new AttributeValue().withN("1"),
                        ":location", new AttributeValue().withS(location)));
    }
}
