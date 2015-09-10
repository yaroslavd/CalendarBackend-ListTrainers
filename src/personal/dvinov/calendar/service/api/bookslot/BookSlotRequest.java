package personal.dvinov.calendar.service.api.bookslot;

public class BookSlotRequest {
    private int year;
    private int month;
    private int day;
    private int slot;
    
    private String location;
    private String trainerId;
    private String clientId;
    
    public BookSlotRequest() {
        
    }
    
    public BookSlotRequest(int year, int month, int day, int slot,
            String location, String trainerId, String clientId) {
        
        this.year = year;
        this.month = month;
        this.day = day;
        this.slot = slot;
        this.location = location;
        this.trainerId = trainerId;
        this.clientId = clientId;
    }
    
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    
    public int getSlot() {
        return slot;
    }
    public void setSlot(int slot) {
        this.slot = slot;
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
    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
