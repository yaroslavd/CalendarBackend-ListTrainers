package personal.dvinov.calendar.service.core.trainers.dao;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "BookedSlots")
public class BookedSlotDao {
    private String trainerId;
    private String dayPlusSlot;
    private String clientId;
    private Date startTime;
    private Date endTime;
    
    public BookedSlotDao() {
        
    }
    
    public BookedSlotDao(String trainerId,
                       String dayPlusSlot,
                       String clientId,
                       Date startTime,
                       Date endTime) {
        
        this.trainerId = trainerId;
        this.dayPlusSlot = dayPlusSlot;
        this.clientId = clientId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @DynamoDBHashKey
    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    @DynamoDBRangeKey
    public String getDayPlusSlot() {
        return dayPlusSlot;
    }

    public void setDayPlusSlot(String dayPlusSlot) {
        this.dayPlusSlot = dayPlusSlot;
    }

    @DynamoDBAttribute
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @DynamoDBAttribute
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @DynamoDBAttribute
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(trainerId)
                .append(dayPlusSlot)
                .append(clientId)
                .append(startTime)
                .append(endTime)
        .build();
    }
}
