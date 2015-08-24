package personal.dvinov.calendar.service.api.freeslots;

import java.util.List;
import java.util.Map;

public class FreeSlotsResponse {
    public static class Slots {
        private List<Integer> slots;
        
        public Slots(final List<Integer> slots) {
            this.slots = slots;
        }

        public List<Integer> getSlots() {
            return slots;
        }
    }
    
    private Map<Day, Slots> freeSlots;
    
    public FreeSlotsResponse(final Map<Day, Slots> freeSlots) {
        this.freeSlots = freeSlots;
    }

    public Map<Day, Slots> getFreeSlots() {
        return freeSlots;
    }
}
