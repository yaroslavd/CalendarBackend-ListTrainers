package personal.dvinov.calendar.service.core.trainers.business;

import java.time.Instant;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SlotBusinessObject {
    private final Instant slotStartTime;
    private final Instant slotEndTime;
    private final int slot;
    
    public SlotBusinessObject(Instant slotStartTime, Instant slotEndTime, int slot) {
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
    public String toString() {
        return new ToStringBuilder(this)
                .append(slotStartTime)
                .append(slotEndTime)
                .append(slot)
        .build();
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
        
        SlotBusinessObject other = (SlotBusinessObject) obj;
        
        return new EqualsBuilder()
                .append(slotStartTime, other.getSlotStartTime())
                .append(slotEndTime, other.getSlotEndTime())
                .append(slot, other.getSlot())
        .build();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(slotStartTime)
                .append(slotEndTime)
                .append(slot)
        .build();
    }
}
