package personal.dvinov.calendar.service.core.trainers.dao;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Trainers")
public class TrainerDao {
    private String id;
    private String location;
    private String name;
    private boolean active;
    
    public TrainerDao() {
        
    }
    
    public TrainerDao(final String id, final String location, final String name, final boolean active) {
        this.id = id;
        this.location = location;
        this.name = name;
        this.active = active;
    }

    @DynamoDBHashKey
    public String getId() {
        return id;
    }

    @DynamoDBAttribute
    public String getLocation() {
        return location;
    }
    
    @DynamoDBAttribute
    public String getName() {
        return name;
    }
    
    @DynamoDBAttribute
    public boolean isActive() {
        return active;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setLocation(final String location) {
        this.location = location;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setActive(final boolean active) {
        this.active = active;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(id)
                .append(location)
                .append(name)
                .append(active)
        .build();
    }

}
