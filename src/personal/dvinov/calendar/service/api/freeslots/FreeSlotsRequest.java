package personal.dvinov.calendar.service.api.freeslots;

public class FreeSlotsRequest {
    private Day inclusiveStartDay;
    private Day inclusiveEndDay;
    private String trainerId;
    
    public FreeSlotsRequest() {
        
    }
    
    public FreeSlotsRequest(final Day inclusiveStartDay, final Day inclusiveEndDay, final String trainerId) {
        this.inclusiveStartDay = inclusiveStartDay;
        this.inclusiveEndDay = inclusiveEndDay;
        this.trainerId = trainerId;
    }

    public Day getInclusiveStartDay() {
        return inclusiveStartDay;
    }

    public void setInclusiveStartDay(Day inclusiveStartDay) {
        this.inclusiveStartDay = inclusiveStartDay;
    }

    public Day getInclusiveEndDay() {
        return inclusiveEndDay;
    }

    public void setInclusiveEndDay(Day inclusiveEndDay) {
        this.inclusiveEndDay = inclusiveEndDay;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }
}
