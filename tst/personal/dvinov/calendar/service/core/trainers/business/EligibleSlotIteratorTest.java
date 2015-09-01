package personal.dvinov.calendar.service.core.trainers.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class EligibleSlotIteratorTest {
    private static final EligibleSlotConfiguration SLOT_CONFIGURATION = EligibleSlotConfiguration.WEEKDAYS_9_TO_5;
    private static final ZoneId ZONE_ID = ZoneId.systemDefault();
    
    @Test
    public void skipsWeekends() {
        assertExpectedSlots(LocalDateTime.of(2015, Month.AUGUST, 28, 16, 0), // Friday
                            LocalDateTime.of(2015, Month.AUGUST, 31, 10, 0), // Monday
                            28, 16,
                            31, 9
        );
    }
    
    @Test
    public void skipsMornings() {
        assertExpectedSlots(LocalDateTime.of(2015, Month.AUGUST, 28, 05, 0),
                            LocalDateTime.of(2015, Month.AUGUST, 28, 10, 0),
                            28, 9
        );
    }
    
    @Test
    public void skipsEvenings() {
        assertExpectedSlots(LocalDateTime.of(2015, Month.AUGUST, 27, 16, 0),
                            LocalDateTime.of(2015, Month.AUGUST, 28, 10, 0),
                            27, 16,
                            28, 9
        );
    }
    
    @Test
    public void startOnHourIncludesThatHour() {
        assertExpectedSlots(LocalDateTime.of(2015, Month.AUGUST, 27, 9, 0),
                            LocalDateTime.of(2015, Month.AUGUST, 27, 10, 0),
                            27, 9
        );
    }
    
    @Test
    public void startAfterHourDoesNotIncludeThatHour() {
        assertExpectedSlots(LocalDateTime.of(2015, Month.AUGUST, 27, 9, 5),
                            LocalDateTime.of(2015, Month.AUGUST, 27, 10, 0)
        );
    }
    
    private Iterable<SlotBusinessObject> getIterable(
            final LocalDateTime start, final LocalDateTime end) {
        
        return new Iterable<SlotBusinessObject>() {
            
            @Override
            public Iterator<SlotBusinessObject> iterator() {
                return new EligibleSlotIterator(
                        SLOT_CONFIGURATION,
                        ZONE_ID,
                        start,
                        end);
            }
        };
    }
    
    /**
     * Returns hour-long slot in August 2015 (to simplify tests)
     * 
     * @param day
     * @param hour
     * @return
     */
    private SlotBusinessObject slotOnDayAndStartingHourInAugust2015(final int day, final int hour) {
        return new SlotBusinessObject(
                fromLdt(LocalDateTime.of(2015, Month.AUGUST, day, hour, 0)),
                fromLdt(LocalDateTime.of(2015, Month.AUGUST, day, hour + 1, 0)),
                hour - SLOT_CONFIGURATION.getBusinessOpen().getHour()
        );
    }
    
    private Instant fromLdt(final LocalDateTime ldt) {
        return ldt.atZone(ZONE_ID).toInstant();
    }
    
    /**
     * 
     * @param start start of interval
     * @param end end of interval
     * @param expectedSlotDaysAndHours expected slots as alternating days and hours
     */
    private void assertExpectedSlots(
            final LocalDateTime start,
            final LocalDateTime end,
            int... expectedSlotDaysAndHours) {
        
        assertTrue("expectedSlotDaysAndHours must have even number of elements "
                + "alternating as day in August and slot start hour",
                expectedSlotDaysAndHours.length % 2 == 0);
        
        final Iterable<SlotBusinessObject> slotCollection = getIterable(start, end);
        final List<SlotBusinessObject> slots = ImmutableList.copyOf(slotCollection);

        final ImmutableList.Builder<SlotBusinessObject> expectedBuilder = ImmutableList.builder();
        for (int i = 0; i < expectedSlotDaysAndHours.length; i += 2) {
            final SlotBusinessObject slot = slotOnDayAndStartingHourInAugust2015(
                    expectedSlotDaysAndHours[i], expectedSlotDaysAndHours[i + 1]);
            expectedBuilder.add(slot);
        }
        final List<SlotBusinessObject> expected = expectedBuilder.build();
        
        assertEquals(expected, slots);
    }
}
