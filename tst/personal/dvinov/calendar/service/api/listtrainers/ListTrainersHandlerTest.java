package personal.dvinov.calendar.service.api.listtrainers;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.common.collect.ImmutableList;

import personal.dvinov.calendar.service.api.listtrainers.ListTrainersHandler;
import personal.dvinov.calendar.service.api.listtrainers.ListTrainersRequest;
import personal.dvinov.calendar.service.api.listtrainers.ListTrainersResponse;
import personal.dvinov.calendar.service.core.trainers.dao.Trainer;
import personal.dvinov.calendar.service.core.trainers.dao.TrainerLoader;

public class ListTrainersHandlerTest {
    private static final String FAKE_TRAINER_NAME = "Yaro";
    private static final String FAKE_TRAINER_ID = "1";
    private static final String FAKE_LOCATION = "Seattle";
    private static final List<Trainer> FAKE_TRAINERS = ImmutableList.of(
            new Trainer(FAKE_TRAINER_ID, FAKE_LOCATION, FAKE_TRAINER_NAME, true));
    
    @Mock private TrainerLoader loader;
    
    private ListTrainersHandler handler;
    private Context ctx;
    
    @Before
    public void setUp() {
        initMocks(this);
        handler = new ListTrainersHandler(loader);
        ctx = createContext();
    }

    @Test(expected=NullPointerException.class)
    public void nullLocationThrowsNullPointerException() {
        final ListTrainersRequest request = new ListTrainersRequest();
        
        handler.handleRequest(request, ctx);
    }
    
    @Test
    public void afterSavingTrainerReturnsTheTrainer() {
        when(loader.listActiveTrainers(FAKE_LOCATION)).thenReturn(FAKE_TRAINERS);
        final ListTrainersRequest request = new ListTrainersRequest(FAKE_LOCATION);

        final ListTrainersResponse output = handler.handleRequest(request, ctx);

        assertEquals(1, output.getTrainers().size()); // we should only get back the one "fake" trainer
        final ListTrainersResponse.Trainer resultTrainer = output.getTrainers().get(0);
        assertEquals(FAKE_TRAINER_ID, resultTrainer.getId());
        assertEquals(FAKE_TRAINER_NAME, resultTrainer.getName());
    }
    
    private Context createContext() {
        final TestContext ctx = new TestContext();

        ctx.setFunctionName("Your Function Name");

        return ctx;
    }
}
