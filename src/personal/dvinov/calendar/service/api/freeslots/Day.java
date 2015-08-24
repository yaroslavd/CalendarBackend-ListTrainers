package personal.dvinov.calendar.service.api.freeslots;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Day {
    private int year;
    private int month;
    private int day;
    
    public Day() {
        
    }
    
    public Day(final int year, final int month, final int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
    
    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }
    
    public void setYear(final int year) {
        this.year = year;
    }

    public void setMonth(final int month) {
        this.month = month;
    }

    public void setDay(final int day) {
        this.day = day;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(year)
                .append(month)
                .append(day)
        .build();
    }
}
