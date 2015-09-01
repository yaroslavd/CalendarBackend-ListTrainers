package personal.dvinov.calendar.service.api.freeslots;

import java.util.Collection;
import java.util.Map;

public class FreeSlotsResponse {
    public static class Slot {
        private int slotIndex;
        private int startHour;
        private int startMinute;
        private int endHour;
        private int endMinute;
        
        public Slot(final int slotIndex,
                    final int startHour,
                    final int startMinute,
                    final int endHour,
                    final int endMinute) {
            
            this.slotIndex = slotIndex;
            this.startHour = startHour;
            this.startMinute = startMinute;
            this.endHour = endHour;
            this.endMinute = endMinute;
        }

        public int getSlotIndex() {
            return slotIndex;
        }

        public int getStartHour() {
            return startHour;
        }

        public int getStartMinute() {
            return startMinute;
        }

        public int getEndHour() {
            return endHour;
        }

        public int getEndMinute() {
            return endMinute;
        }
    }
    
    private Map<Day, Collection<Slot>> freeSlots;
    
    public FreeSlotsResponse(final Map<Day, Collection<Slot>> freeSlots) {
        this.freeSlots = freeSlots;
    }

    public Map<Day, Collection<Slot>> getFreeSlots() {
        return freeSlots;
    }
}
