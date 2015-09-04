package personal.dvinov.calendar.service.api.freeslots;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class FreeSlotsResponse {
    public static class Slot {
        private int year;
        private int month;
        private int day;
        private int slotIndex;
        private int startHour;
        private int startMinute;
        private int endHour;
        private int endMinute;
        
        public Slot(int year, int month, int day, int slotIndex,
                int startHour, int startMinute, int endHour, int endMinute) {
            
            this.year = year;
            this.month = month;
            this.day = day;
            this.slotIndex = slotIndex;
            this.startHour = startHour;
            this.startMinute = startMinute;
            this.endHour = endHour;
            this.endMinute = endMinute;
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
        
        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append(year)
                    .append(month)
                    .append(day)
                    .append(slotIndex)
                    .append(startHour)
                    .append(startMinute)
                    .append(endHour)
                    .append(endMinute)
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
            
            Slot other = (Slot) obj;
            
            return new EqualsBuilder()
                    .append(year, other.year)
                    .append(month, other.month)
                    .append(day, other.day)
                    .append(slotIndex, other.slotIndex)
                    .append(startHour, other.startHour)
                    .append(startMinute, other.startMinute)
                    .append(endHour, other.endHour)
                    .append(endMinute, other.endMinute)
            .build();
        }
    }

    private List<Slot> freeSlots;
        
    public FreeSlotsResponse(List<Slot> freeSlots) {
        this.freeSlots = freeSlots;
    }

    public List<Slot> getFreeSlots() {
        return freeSlots;
    }
}
