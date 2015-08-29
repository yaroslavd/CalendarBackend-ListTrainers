package personal.dvinov.calendar.service.core.trainers.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static personal.dvinov.calendar.service.fixture.TestUtils.createDynamoTable;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class TrainerLoaderTest {
    private static final String TABLE_NAME = "Trainers";
    
    private static final String TEST_TRAINER_ID = "1";
    private static final String TEST_TRAINER_NAME = "Yaro";
    private static final String TEST_LOCATION = "Seattle";
    
    private static DynamoDBMapper mapper;
    private static TrainerLoader loader;
    private static AmazonDynamoDBClient client;
    
    @BeforeClass
    public static void setUpSuite() {
        client = new AmazonDynamoDBClient();
        // http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Tools.DynamoDBLocal.html
        client.setEndpoint("http://localhost:8010");
        
        mapper = new DynamoDBMapper(client);
        loader = new TrainerLoader(mapper);
    }
    
    @Before
    public void setUpTest() {
        createDynamoTable(client, mapper, TABLE_NAME, TrainerDao.class);
    }
    
    @Test
    public void listActiveTrainersReturnsSavedTrainer() {
        final TrainerDao savedTrainer = getTestTrainer(true);
        mapper.save(savedTrainer);
        final List<TrainerDao> loadedTrainers = loader.listActiveTrainers(TEST_LOCATION);
        
        assertEquals(1, loadedTrainers.size()); // should only return 1 trainer
        final TrainerDao loadedTrainer = loadedTrainers.get(0);
        assertEquals(savedTrainer.getId(), loadedTrainer.getId()); // we only care about id and name
        assertEquals(savedTrainer.getName(), loadedTrainer.getName()); // so not calling equals()
    }
    
    @Test(expected=NullPointerException.class)
    public void listActiveTrainersWithNullLocationThrowsNullPointerException() {
        loader.listActiveTrainers(null);
    }
    
    @Test
    public void listActiveTrainersWithExistingLocationReturnsTrainer() {
        mapper.save(getTestTrainer(true));
        assertResultHasTrainers(true);
    }
    
    @Test
    public void listActiveTrainersWithNonExistingLocationReturnsNoTrainers() {
        saveTestTrainer(true);
        assertTrue(loader.listActiveTrainers("Fake Location").isEmpty());
    }
    
    @Test
    public void listActiveTrainersWithNoActiveTrainersReturnsNoTrainers() {
        saveTestTrainer(false);
        assertResultHasTrainers(false);
    }
    
    private TrainerDao getTestTrainer(final boolean active) {
        return new TrainerDao(TEST_TRAINER_ID, TEST_LOCATION, TEST_TRAINER_NAME, active);
    }
    
    private void saveTestTrainer(final boolean active) {
        mapper.save(getTestTrainer(active));
    }
    
    private void assertResultHasTrainers(final boolean returnsTrainers) {
        assertFalse(loader.listActiveTrainers(TEST_LOCATION).isEmpty() == returnsTrainers);
    }
}