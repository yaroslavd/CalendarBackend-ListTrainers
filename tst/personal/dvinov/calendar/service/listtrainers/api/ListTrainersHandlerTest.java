package personal.dvinov.calendar.service.listtrainers.api;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import personal.dvinov.calendar.service.listtrainers.api.ListTrainersHandler;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class ListTrainersHandlerTest {

    @BeforeClass
    public static void createInput() throws IOException {
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("Your Function Name");

        return ctx;
    }

    @Test
    public void testListTrainersHandler() {
        ListTrainersHandler handler = new ListTrainersHandler();
        Context ctx = createContext();

        Object output = handler.handleRequest(null, ctx);

        // TODO: validate output here if needed.
        if (output != null) {
            System.out.println(output.toString());
        }
    }
}
