package personal.dvinov.calendar.service.core.trainers.business;

import java.time.Instant;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class BookedSlotBusinessObject {
    private final Instant slotStartTime;
    private final Instant slotEndTime;
    private final int slot;
    
    public BookedSlotBusinessObject(Instant slotStartTime,
            Instant slotEndTime,
            int slot) {

        this.slotStartTime = slotStartTime;
        this.slotEndTime = slotEndTime;
        this.slot = slot;
    }

    public Instant getSlotStartTime() {
        return slotStartTime;
    }

    public Instant getSlotEndTime() {
        return slotEndTime;
    }

    public int getSlot() {
        return slot;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (obj == this) {
            return true;
        }
        
        if (obj.getClass() != getClass()) {
            return false;
        }
        
        BookedSlotBusinessObject other = (BookedSlotBusinessObject) obj;
        
        return new EqualsBuilder()
                .append(slotStartTime, other.getSlotStartTime())
                .append(slotEndTime, other.getSlotEndTime())
                .append(slot, other.getSlot())
        .build();
    }
}
