package personal.dvinov.calendar.service.listtrainers.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.common.collect.ImmutableList;

import personal.dvinov.calendar.service.core.trainers.dao.Trainer;
import personal.dvinov.calendar.service.core.trainers.dao.TrainerLoader;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class ListTrainersHandlerTest {
    private static final String FAKE_TRAINER_NAME = "Yaro";
    private static final String FAKE_TRAINER_ID = "1";
    private static final String FAKE_LOCATION = "Seattle";
    private static final List<Trainer> FAKE_TRAINERS = ImmutableList.of(
            new Trainer(FAKE_TRAINER_ID, FAKE_LOCATION, FAKE_TRAINER_NAME, true));

    @BeforeClass
    public static void createInput() throws IOException {
    }

    private Context createContext() {
        final TestContext ctx = new TestContext();

        ctx.setFunctionName("Your Function Name");

        return ctx;
    }

    @Test
    public void testListTrainersHandler() {
        final TrainerLoader loader = mock(TrainerLoader.class);
        when(loader.listActiveTrainers(FAKE_LOCATION)).thenReturn(FAKE_TRAINERS);
        
        final ListTrainersRequest request = new ListTrainersRequest(FAKE_LOCATION);
        final ListTrainersHandler handler = new ListTrainersHandler(loader);
        final Context ctx = createContext();

        final ListTrainersResponse output = handler.handleRequest(request, ctx);

        assertEquals(1, output.getTrainers().size()); // we should only get back the one "fake" trainer
        final ListTrainersResponse.Trainer resultTrainer = output.getTrainers().get(0);
        assertEquals(FAKE_TRAINER_ID, resultTrainer.getId());
        assertEquals(FAKE_TRAINER_NAME, resultTrainer.getName());
    }
}
