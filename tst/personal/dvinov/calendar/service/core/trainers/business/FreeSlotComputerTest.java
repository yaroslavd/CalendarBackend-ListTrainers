package personal.dvinov.calendar.service.core.trainers.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.google.common.collect.ImmutableList;

import personal.dvinov.calendar.service.core.trainers.dao.BookedSlotAdapter;

public class FreeSlotComputerTest {
    private static final ZoneId TIME_ZONE = ZoneId.systemDefault();
    private static final String TRAINER_ID = "TRAINER_ID";
    private static final LocalDateTime LOCAL_INTERVAL_START = LocalDateTime.of(2015, 8, 31, 9, 0);
    private static final LocalDateTime LOCAL_INTERVAL_END   = LocalDateTime.of(2015, 8, 31, 10, 0);
    private static final Instant INSTANT_INTERVAL_START = LOCAL_INTERVAL_START.atZone(TIME_ZONE).toInstant();
    private static final Instant INSTANT_INTERVAL_END = LOCAL_INTERVAL_END.atZone(TIME_ZONE).toInstant();
    
    @Mock
    private BookedSlotAdapter bookedSlotAdapter;
    private final Iterable<SlotBusinessObject> eligibleSlots =
            new WeekdayNineToFiveHourLongEligibleSlotProvider(
                    TIME_ZONE,
                    LOCAL_INTERVAL_START,
                    LOCAL_INTERVAL_END);
    private FreeSlotComputer computer;
    
    @Before
    public void setUp() {
        initMocks(this);
        computer = new FreeSlotComputer(bookedSlotAdapter, eligibleSlots);
    }
    
    @Test
    public void unbookedSlotReturnedAsFree() {
        final List<SlotBusinessObject> booked = ImmutableList.of(
        );
        expectedBookedSlotAdapterToReturn(booked);
        
        final List<SlotBusinessObject> computedFree =
                computer.computeFreeSlots(INSTANT_INTERVAL_START, INSTANT_INTERVAL_END, TRAINER_ID);
        
        final List<SlotBusinessObject> expectedFree = ImmutableList.of(
                new SlotBusinessObject(INSTANT_INTERVAL_START, INSTANT_INTERVAL_END, 0)
        );
        assertEquals(expectedFree, computedFree);
    }
    
    @Test
    public void bookedSlotDoesNotGetReturnedAsFree() {
        final List<SlotBusinessObject> booked = ImmutableList.of(
                new SlotBusinessObject(INSTANT_INTERVAL_START, INSTANT_INTERVAL_END, 0)
        );
        expectedBookedSlotAdapterToReturn(booked);
        
        final List<SlotBusinessObject> computedFree =
                computer.computeFreeSlots(INSTANT_INTERVAL_START, INSTANT_INTERVAL_END, TRAINER_ID);
        
        final List<SlotBusinessObject> expectedFree = ImmutableList.of(
        );
        assertEquals(expectedFree, computedFree);
    }
    
    private void expectedBookedSlotAdapterToReturn(final List<SlotBusinessObject> toReturn) {
        when(bookedSlotAdapter.listBookedSlots(
                TRAINER_ID,
                INSTANT_INTERVAL_START,
                INSTANT_INTERVAL_END))
        .thenReturn(toReturn);
    }
}
