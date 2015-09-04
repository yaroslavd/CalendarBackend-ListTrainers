package personal.dvinov.calendar.service.api.freeslots;

import java.time.Instant;

import org.junit.Assert;
import org.junit.Test;

import personal.dvinov.calendar.service.fixture.TestContext;

public class FreeSlotsHandlerIntegrationTest {
    
    private static final String LOCATION = "Seattle";
    private static final Instant WINDOW_CLOSE = Instant.parse("2015-09-01T18:00:00Z");
    private static final Instant WINDOW_START = Instant.parse("2015-09-01T16:00:00Z");
    
    private final FreeSlotsHandler handler = new FreeSlotsHandler();
    
    @Test
    public void twoHourWindowWithNoBookedSlotsReturnsTwoSlots() {
        final FreeSlotsRequest request = new FreeSlotsRequest(
                WINDOW_START,
                WINDOW_CLOSE,
                LOCATION,
                "unknownTrainer");
        
        Assert.assertEquals(2, handler.handleRequest(request, new TestContext()).getFreeSlots().size());
    }
    
    @Test
    public void twoHourWindowWithOneBookedSlotReturnsOneSlot() {
        final FreeSlotsRequest request = new FreeSlotsRequest(
                WINDOW_START,
                WINDOW_CLOSE,
                LOCATION,
                "knownTrainer");
        
        Assert.assertEquals(1, handler.handleRequest(request, new TestContext()).getFreeSlots().size());
    }
}
