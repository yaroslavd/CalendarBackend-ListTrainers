package personal.dvinov.calendar.service.api.listtrainers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import personal.dvinov.calendar.service.api.listtrainers.ListTrainersHandler;
import personal.dvinov.calendar.service.api.listtrainers.ListTrainersRequest;
import personal.dvinov.calendar.service.api.listtrainers.ListTrainersResponse;
import personal.dvinov.calendar.service.api.listtrainers.ListTrainersResponse.Trainer;
import personal.dvinov.calendar.service.fixture.TestContext;

public class ListTrainersHandlerIntegrationTest {
    private static final String KNOWN_TRAINER_NAME = "Yaro";
    private static final String KNOWN_TRAINER_ID = "1";
    private static final String KNOWN_LOCATION = "Seattle";
    private static final Trainer KNOWN_TRAINER =
            new Trainer(KNOWN_TRAINER_ID, KNOWN_TRAINER_NAME);
    
    private ListTrainersHandler handler;
    private Context ctx;

    @Before
    public void setUp() {
        handler = new ListTrainersHandler();
        ctx = createContext();
    }
    
    @Test
    public void knownLocationReturnsAtLeastKnownTrainer() {
        final ListTrainersRequest request = new ListTrainersRequest(KNOWN_LOCATION);
        
        final ListTrainersResponse output = handler.handleRequest(request, ctx);

        assertFalse(output.getTrainers().isEmpty());
        assertTrue(output.getTrainers().contains(KNOWN_TRAINER));
    }
    
    @Test
    public void unknownLocationReturnsNoTrainers() {
        final ListTrainersRequest request = new ListTrainersRequest("FAKE_LOCATION");

        final ListTrainersResponse output = handler.handleRequest(request, ctx);

        assertTrue(output.getTrainers().isEmpty());
    }
    
    private Context createContext() {
        final TestContext ctx = new TestContext();

        ctx.setFunctionName("Your Function Name");

        return ctx;
    }
}
