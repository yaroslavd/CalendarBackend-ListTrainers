package personal.dvinov.calendar.service.listtrainers.api;

public class ListTrainersRequest {
    private String location;
    
    public ListTrainersRequest(final String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
