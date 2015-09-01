package personal.dvinov.calendar.service.core.trainers.business;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.common.collect.ImmutableSet;

import personal.dvinov.calendar.service.core.trainers.dao.BookedSlotAdapter;

public class FreeSlotComputer {
    private final BookedSlotAdapter bookedSlotAdapter;
    
    public FreeSlotComputer(
            final BookedSlotAdapter bookedSlotAdapter) {
        
        this.bookedSlotAdapter = bookedSlotAdapter;
    }
    
    public List<SlotBusinessObject> computeFreeSlots(
            final Instant start,
            final Instant end,
            final ZoneId zoneId,
            final String trainerId) {
        
        final List<SlotBusinessObject> bookedSlots =
                bookedSlotAdapter.listBookedSlots(trainerId, start, end);
        // Convert to Set for O(1) lookup
        final Set<SlotBusinessObject> bookedSlotSet = ImmutableSet.copyOf(bookedSlots);
        
        final Iterable<SlotBusinessObject> eligibleSlots = new WeekdayNineToFiveHourLongEligibleSlotProvider(
                zoneId, LocalDateTime.ofInstant(start, zoneId), LocalDateTime.ofInstant(end, zoneId));

        return StreamSupport.stream(eligibleSlots.spliterator(), false)
                .filter(eligibleSlot -> !bookedSlotSet.contains(eligibleSlot))
                .collect(Collectors.toList());
    }
}
