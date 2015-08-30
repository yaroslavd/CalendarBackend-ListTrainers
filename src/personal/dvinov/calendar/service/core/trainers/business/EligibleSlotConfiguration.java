package personal.dvinov.calendar.service.core.trainers.business;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import static java.time.DayOfWeek.*;

public enum EligibleSlotConfiguration {
    WEEKDAYS_9_TO_5(
            LocalTime.of(9, 0),
            LocalTime.of(17, 0),
            ImmutableSet.of(
                    MONDAY,
                    TUESDAY,
                    WEDNESDAY,
                    THURSDAY,
                    FRIDAY
            )
    );
    
    private final LocalTime businessOpen;
    private final LocalTime businessClose;
    private final Set<DayOfWeek> daysOfWeekOpen;
    
    private EligibleSlotConfiguration(
            final LocalTime businessOpen,
            final LocalTime businessClose,
            final Set<DayOfWeek> daysOfWeekOpen) {
        
        this.businessOpen = businessOpen;
        this.businessClose = businessClose;
        this.daysOfWeekOpen = daysOfWeekOpen;
    }

    public LocalTime getBusinessOpen() {
        return businessOpen;
    }

    public LocalTime getBusinessClose() {
        return businessClose;
    }

    public Set<DayOfWeek> getDaysOfWeekOpen() {
        return daysOfWeekOpen;
    }
}
