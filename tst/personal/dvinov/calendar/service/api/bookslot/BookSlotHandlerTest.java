package personal.dvinov.calendar.service.api.bookslot;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;

import personal.dvinov.calendar.service.api.common.LocationConfiguration;
import personal.dvinov.calendar.service.core.trainers.dao.BookedSlotAdapter;
import personal.dvinov.calendar.service.fixture.TestContext;

public class BookSlotHandlerTest {
    private static final String CLIENT_ID = "clientId";
    private static final BookSlotRequest VALID_BOOK_SLOT_REQUEST = new BookSlotRequest(
            2015, 9, 10, 1, "Seattle", "trainerId", CLIENT_ID);

    private BookSlotHandler handler;
    
    @Mock private BookedSlotAdapter adapter;
    
    @Before
    public void setUpTest() {
        initMocks(this);
        handler = new BookSlotHandler(adapter, new LocationConfiguration());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void unauthenticatedAccessThrowsIllegalArgumentException() {
        handler.handleRequest(VALID_BOOK_SLOT_REQUEST, contextWithClient(Optional.empty()));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void mismatchedCallerThrowsIllegalArgumentException() {
        handler.handleRequest(VALID_BOOK_SLOT_REQUEST, contextWithClient(Optional.of("mismatchingClient")));
    }
    
    @Test
    public void authorizedCallerSucceeds() {
        handler.handleRequest(VALID_BOOK_SLOT_REQUEST, contextWithClient(Optional.of(CLIENT_ID)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void bookingAlreadyBookedSlotThrowsIllegalArgumentException() {
        doThrow(new ConditionalCheckFailedException("already booked"))
        .when(adapter).bookSlot(
                VALID_BOOK_SLOT_REQUEST.getTrainerId(),
                VALID_BOOK_SLOT_REQUEST.getClientId(),
                VALID_BOOK_SLOT_REQUEST.getYear(),
                VALID_BOOK_SLOT_REQUEST.getMonth(),
                VALID_BOOK_SLOT_REQUEST.getDay(),
                VALID_BOOK_SLOT_REQUEST.getSlot(),
                new LocationConfiguration().getZoneId(VALID_BOOK_SLOT_REQUEST.getLocation()));
        
        handler.handleRequest(VALID_BOOK_SLOT_REQUEST, contextWithClient(Optional.of(CLIENT_ID)));
    }
    
    private TestContext contextWithClient(final Optional<String> client) {
        final TestContext context = new TestContext();
        context.setIdentity(new CognitoIdentity() {
            
            @Override
            public String getIdentityPoolId() {
                return null;
            }
            
            @Override
            public String getIdentityId() {
                return client.orElse(null);
            }
        });

        return context;
    }
}
