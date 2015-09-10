package personal.dvinov.calendar.service.api.bookslot;

import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import personal.dvinov.calendar.service.api.common.LocationConfiguration;
import personal.dvinov.calendar.service.api.common.util.ContextLogger;
import personal.dvinov.calendar.service.core.trainers.dao.BookedSlotAdapter;

public class BookSlotHandler implements RequestHandler<BookSlotRequest, BookSlotResponse> {
    
    private final BookedSlotAdapter bookedSlotAdapter;
    private final LocationConfiguration locationConfiguration;
    
    public BookSlotHandler() {
        final AmazonDynamoDB dynamoClient = new AmazonDynamoDBClient();
        final DynamoDBMapper mapper = new DynamoDBMapper(dynamoClient);
        
        this.bookedSlotAdapter = new BookedSlotAdapter(mapper);
        this.locationConfiguration = new LocationConfiguration();
    }
    
    public BookSlotHandler(
            final BookedSlotAdapter bookedSlotAdapter, final LocationConfiguration locationConfiguration) {
        
        this.bookedSlotAdapter = bookedSlotAdapter;
        this.locationConfiguration = locationConfiguration;
    }

    @Override
    public BookSlotResponse handleRequest(BookSlotRequest input, Context context) {
        context.getLogger().log("Input: " + input);
        ContextLogger.logContext(context);
        
        // Authentication check
        final Optional<String> boxedCallerIdentity =
                Optional.ofNullable(context.getIdentity())
                .map(CognitoIdentity::getIdentityId);
        Validate.isTrue(boxedCallerIdentity.isPresent(),
                "Authentication exception: Caller must be authenticated to book a slot");
        
        // Authorization check
        final String callerIdentity = boxedCallerIdentity.get();
        Validate.isTrue(callerIdentity.equals(input.getTrainerId())
                || callerIdentity.equals(input.getClientId()),
                "Authorization exception: cognito identity id must " +
                "match either the clientId or the trainerId in the request");
        
        try {
            bookedSlotAdapter.bookSlot(input.getTrainerId(), input.getClientId(),
                    input.getYear(), input.getMonth(), input.getDay(), input.getSlot(),
                    locationConfiguration.getZoneId(input.getLocation()));
        }
        catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("Conflict exception: someone else just booked this slot", e);
        }
        
        return new BookSlotResponse();
    }


}
