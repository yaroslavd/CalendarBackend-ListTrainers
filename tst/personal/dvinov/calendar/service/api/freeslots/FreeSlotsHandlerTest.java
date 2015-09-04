package personal.dvinov.calendar.service.api.freeslots;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.common.collect.ImmutableList;

import personal.dvinov.calendar.service.api.freeslots.FreeSlotsResponse.Slot;
import personal.dvinov.calendar.service.core.trainers.business.FreeSlotComputer;
import personal.dvinov.calendar.service.core.trainers.business.SlotBusinessObject;
import personal.dvinov.calendar.service.fixture.TestContext;

public class FreeSlotsHandlerTest {
    private final Instant START = Instant.ofEpochMilli(1);
    private final Instant END = Instant.ofEpochMilli(10);
    private final ZoneId ZONE = ZoneId.systemDefault();
    private final String TRAINER_ID = "TRAINER_ID";
    private final String LOCATION = "Seattle";
    
    private final Context CONTEXT = new TestContext();
    
    @Mock private FreeSlotComputer computer;
    
    @Before
    public void setUp() {
        initMocks(this);
    }
    
    @Test
    public void noFreeSlotsReturnsEmptyResponse() {
        assertResponseGivenFreeSlotsResult(ImmutableList.of(), ImmutableList.of());
    }
    
    @Test
    public void freeSlotsReturnsSameSlotInResponse() {
        final Slot expectedSlot = new Slot(2015, 9, 3, 0, 9, 0, 10, 0);
        final SlotBusinessObject slotFromComputer = slotFromComputer(expectedSlot);
        
        assertResponseGivenFreeSlotsResult(
                ImmutableList.of(slotFromComputer),
                ImmutableList.of(expectedSlot)
        );
    }

    private SlotBusinessObject slotFromComputer(final Slot expectedSlot) {
        final LocalDateTime slotStartFromComputer = LocalDateTime.of(
                expectedSlot.getYear(), expectedSlot.getMonth(), expectedSlot.getDay(),
                expectedSlot.getStartHour(), expectedSlot.getStartMinute());
        final LocalDateTime slotEndFromComputer = LocalDateTime.of(
                expectedSlot.getYear(), expectedSlot.getMonth(), expectedSlot.getDay(),
                expectedSlot.getEndHour(), expectedSlot.getEndMinute());
        
        return new SlotBusinessObject(
                slotStartFromComputer.atZone(ZONE).toInstant(),
                slotEndFromComputer.atZone(ZONE).toInstant(),
                expectedSlot.getSlotIndex());
    }
    
    private void assertResponseGivenFreeSlotsResult(
            final List<SlotBusinessObject> freeSlotsResult, final List<Slot> expectedResponse) {
        
        final FreeSlotsRequest request = new FreeSlotsRequest(START, END, LOCATION, TRAINER_ID);
        when(computer.computeFreeSlots(START, END, ZONE, TRAINER_ID)).thenReturn(freeSlotsResult);
        
        final FreeSlotsHandler handler = new FreeSlotsHandler(computer);
        
        Assert.assertEquals(expectedResponse, handler.handleRequest(request, CONTEXT).getFreeSlots());
    }
}
