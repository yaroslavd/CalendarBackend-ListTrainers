package personal.dvinov.calendar.service.core.trainers.business;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.common.collect.ImmutableSet;

import personal.dvinov.calendar.service.core.trainers.dao.BookedSlotAdapter;

public class FreeSlotComputer {
    private final BookedSlotAdapter bookedSlotAdapter;
    private final Iterable<SlotBusinessObject> eligibleSlotIterator;
    
    public FreeSlotComputer(
            final BookedSlotAdapter bookedSlotAdapter,
            final Iterable<SlotBusinessObject> eligibleSlotIterator) {
        
        this.bookedSlotAdapter = bookedSlotAdapter;
        this.eligibleSlotIterator = eligibleSlotIterator;
    }
    
    public List<SlotBusinessObject> computeFreeSlots(
            final Instant start,
            final Instant end,
            final String trainerId) {
        
        final List<SlotBusinessObject> bookedSlots =
                bookedSlotAdapter.listBookedSlots(trainerId, start, end);
        // Convert to Set for O(1) lookup
        final Set<SlotBusinessObject> bookedSlotSet = ImmutableSet.copyOf(bookedSlots);

        return StreamSupport.stream(eligibleSlotIterator.spliterator(), false)
                .filter(eligibleSlot -> !bookedSlotSet.contains(eligibleSlot))
                .collect(Collectors.toList());
    }
}
