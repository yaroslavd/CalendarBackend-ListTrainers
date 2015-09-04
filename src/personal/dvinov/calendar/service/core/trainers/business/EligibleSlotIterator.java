package personal.dvinov.calendar.service.core.trainers.business;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class EligibleSlotIterator implements Iterator<SlotBusinessObject> {

    private final LocalTime businessOpen;
    private final LocalTime businessClose;
    private final Set<DayOfWeek> daysOfWeekOpen;
    
    private final ZoneId zoneId;
    
    private final LocalDateTime queryIntervalStart;
    private final LocalDateTime queryIntervalEnd;
    
    private LocalDateTime cursor;
    
    public EligibleSlotIterator(
            final EligibleSlotConfiguration configuration,
            final ZoneId zoneId,
            final LocalDateTime queryIntervalStart,
            final LocalDateTime queryIntervalEnd) {
        
        this.businessOpen = configuration.getBusinessOpen();
        this.businessClose = configuration.getBusinessClose();
        this.daysOfWeekOpen = configuration.getDaysOfWeekOpen();
        this.zoneId = zoneId;
        this.queryIntervalStart = queryIntervalStart;
        this.queryIntervalEnd = queryIntervalEnd;
        
        cursor = firstSlot();
        advanceToNextDayIfNeeded();
        advanceToBusinessOpenIfNeeded();
    }
    
    @Override
    public boolean hasNext() {
        return !currentSlotEnd().isAfter(queryIntervalEnd);
    }

    @Override
    public SlotBusinessObject next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        final SlotBusinessObject result = slotFromStart(cursor);
        
        cursor = cursor.plusHours(1);
        advanceToNextDayIfNeeded();
        
        return result;
    }
    
    private LocalDateTime firstSlot() {
        final LocalDateTime truncatedToHour = queryIntervalStart.truncatedTo(ChronoUnit.HOURS);
        
        if (truncatedToHour.equals(queryIntervalStart)) {
            return queryIntervalStart;
        }
        
        return truncatedToHour.plusHours(1);
    }
    
    private void advanceToNextDayIfNeeded() {
        if (currentSlotEnd().toLocalTime().isAfter(businessClose)) {
            advanceToNextDay();
        }
        
        while (!daysOfWeekOpen.contains(cursor.getDayOfWeek())) {
            advanceToNextDay();
        }
    }
    
    private void advanceToBusinessOpenIfNeeded() {
        if (cursor.toLocalTime().isBefore(businessOpen)) {
            cursor = LocalDateTime.of(cursor.toLocalDate(), businessOpen);
        }
    }
    
    private void advanceToNextDay() {
        cursor = LocalDateTime.of(cursor.plusDays(1).toLocalDate(), businessOpen);
    }

    private SlotBusinessObject slotFromStart(final LocalDateTime slotStart) {
        return new SlotBusinessObject(
                fromLdt(slotStart),
                fromLdt(slotStart.plusHours(1)),
                slotStart.getHour() - businessOpen.getHour());
    }
    
    private Instant fromLdt(final LocalDateTime ldt) {
        return ldt.atZone(zoneId).toInstant();
    }
    
    private LocalDateTime currentSlotEnd() {
        return cursor.plusHours(1);
    }
}