package personal.dvinov.calendar.service.api.listtrainers;

public class ListTrainersRequest {
    private String location;
    
    public ListTrainersRequest() {
        
    }
    
    public ListTrainersRequest(final String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }
}
