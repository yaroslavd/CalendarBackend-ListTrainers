package personal.dvinov.calendar.service.core.trainers.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static personal.dvinov.calendar.service.fixture.TestUtils.createDynamoTable;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import personal.dvinov.calendar.service.core.trainers.business.BookedSlotBusinessObject;

public class BookedSlotAdapterTest {
    private static final String TABLE_NAME = "BookedSlots";
    
    private static final String TRAINER_ID = "trainerId";
    private static final String CLIENT_ID = "clientId";
    private static final Instant START_DATE = Instant.parse("2015-08-29T10:15:30Z");
    private static final Instant END_DATE = Instant.parse("2015-08-30T10:15:30Z");
    
    private static DynamoDBMapper mapper;
    private static BookedSlotAdapter adapter;
    private static AmazonDynamoDBClient client;
    
    @BeforeClass
    public static void setUpSuite() {
        client = new AmazonDynamoDBClient();
        // http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Tools.DynamoDBLocal.html
        client.setEndpoint("http://localhost:8010");
        
        mapper = new DynamoDBMapper(client);
        adapter = new BookedSlotAdapter(mapper);
    }
    
    @Before
    public void setUpTest() {
        createDynamoTable(client, mapper, TABLE_NAME, BookedSlotDao.class);
    }
    
    @Test
    public void afterNoInsertionsListReturnsEmptyList() {
        final List<BookedSlotBusinessObject> result = adapter.listBookedSlots(TRAINER_ID, START_DATE, END_DATE);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void afterInsertionWithinIntervalListReturnsSlot() {
        final Instant slotStartTime = Instant.parse("2015-08-30T09:00:30Z");
        final Instant slotEndTime = Instant.parse("2015-08-30T10:00:30Z");
        insertSlot(slotStartTime, slotEndTime, "2015-08-30-01");

        final List<BookedSlotBusinessObject> result = adapter.listBookedSlots(TRAINER_ID, START_DATE, END_DATE);
        
        assertEquals(1, result.size());
        final BookedSlotBusinessObject expectedSlot =
                new BookedSlotBusinessObject(slotStartTime, slotEndTime, 1);
        assertEquals(expectedSlot, result.get(0));
    }
    
    @Test
    public void afterInsertionOutsideIntervalListReturnsNoSlots() {
        insertSlot(
                Instant.parse("2015-08-31T09:00:30Z"),
                Instant.parse("2015-08-31T10:00:30Z"),
                "2015-08-30-01");

        final List<BookedSlotBusinessObject> result = adapter.listBookedSlots(TRAINER_ID, START_DATE, END_DATE);
        
        assertTrue(result.isEmpty());
    }
    
    /**
     * Insert slot into Dynamo and return it
     * 
     * @param slotStart
     * @param slotEnd
     * @param dayPlusSlot
     * @return
     */
    private BookedSlotDao insertSlot(final Instant slotStart,
                                    final Instant slotEnd,
                                    final String dayPlusSlot) {
        
        final BookedSlotDao slot = new BookedSlotDao(
                TRAINER_ID, dayPlusSlot, CLIENT_ID, Date.from(slotStart), Date.from(slotEnd));
        
        mapper.save(slot);
        
        return slot;
    }
}