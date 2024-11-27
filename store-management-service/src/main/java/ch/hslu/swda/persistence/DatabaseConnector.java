package ch.hslu.swda.persistence;

import ch.hslu.swda.entities.Order;
import ch.hslu.swda.entities.Store;
import ch.hslu.swda.entities.StoreArticle;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import org.bson.UuidRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static dev.morphia.query.experimental.filters.Filters.eq;


public class DatabaseConnector {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseConnector.class);

    private final Datastore datastore;

    private final static String DATABASE_URI = "mongodb://localhost:27017";
    private final static String DATABASE_NAME = "storemanagement";

    public DatabaseConnector() {
        String mongoUri = System.getenv().getOrDefault("MONGO_URI", DATABASE_URI);
        LOG.info("Mongo URI is: {}", mongoUri);
        ConnectionString connectionString = new ConnectionString(mongoUri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .build();
        datastore = Morphia.createDatastore(MongoClients.create(settings), DATABASE_NAME);
        datastore.getMapper().map(Order.class);
        datastore.getMapper().map(Store.class);
        datastore.ensureIndexes();
    }

    public void storeStore(Store store) {
        StoreWrapper wrapper = new StoreWrapper(store);
        List<DBOrder> dbOrders = wrapper.getDbOrders();
        DBStore dbstore = wrapper.getDbStore();
        datastore.save(dbstore);
        for (DBOrder order : dbOrders) {
            datastore.save(order);
        }
    }

    public Store getStore(UUID storeId) {
        DBStore dbStore = datastore.find(DBStore.class)
                .filter(eq("_id", storeId))
                .first();
        List<DBOrder> dbOrders = new ArrayList<>();
        if (dbStore.getOpenOrders() != null) {
            List<UUID> orderIds = dbStore.getOpenOrders();
            for (UUID id : orderIds) {
                DBOrder order = datastore.find(DBOrder.class)
                        .filter(eq("_id", id))
                        .first();
                dbOrders.add(order);
            }
        }
        StoreWrapper wrapper = new StoreWrapper(dbOrders, dbStore);
        return wrapper.getStore();
    }

}
