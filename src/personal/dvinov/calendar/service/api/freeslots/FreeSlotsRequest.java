package personal.dvinov.calendar.service.api.freeslots;

import java.time.Instant;
import java.time.ZonedDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class FreeSlotsRequest {
    private Instant queryIntervalStart;
    private Instant queryIntervalEnd;
    private String location;
    private String trainerId;
    
    public FreeSlotsRequest() {
        
    }
    
    public FreeSlotsRequest(
            final Instant queryIntervalStart,
            final Instant queryIntervalEnd,
            final String location,
            final String trainerId) {
        
        this.queryIntervalStart = queryIntervalStart;
        this.queryIntervalEnd = queryIntervalEnd;
        this.location = location;
        this.trainerId = trainerId;
    }

    public Instant getQueryIntervalStart() {
        return queryIntervalStart;
    }

    public void setQueryIntervalStart(final String queryIntervalStart) {
        this.queryIntervalStart = ZonedDateTime.parse(queryIntervalStart).toInstant();
    }

    public Instant getQueryIntervalEnd() {
        return queryIntervalEnd;
    }

    public void setQueryIntervalEnd(final String queryIntervalEnd) {
        this.queryIntervalEnd = ZonedDateTime.parse(queryIntervalEnd).toInstant();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(final String trainerId) {
        this.trainerId = trainerId;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(queryIntervalStart)
                .append(queryIntervalEnd)
                .append(location)
                .append(trainerId)
        .build();
    }
}
