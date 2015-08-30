package personal.dvinov.calendar.service.fixture;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.Tables;

public class TestUtils {
    /**
     * Creates Dynamo table from POJO
     * 
     * @param client
     * @param mapper
     * @param tableName
     * @param tablePojo
     */
    public static <T> void createDynamoTable(
            final AmazonDynamoDBClient client,
            final DynamoDBMapper mapper,
            final String tableName,
            final Class<T> tablePojo) {
        
        if (Tables.doesTableExist(client, tableName)) {
            client.deleteTable(tableName);
        }
        
        final CreateTableRequest createTableRequest = mapper.generateCreateTableRequest(tablePojo);
        client.createTable(createTableRequest.withProvisionedThroughput(
                new ProvisionedThroughput(1L, 1L)));
    }
}
