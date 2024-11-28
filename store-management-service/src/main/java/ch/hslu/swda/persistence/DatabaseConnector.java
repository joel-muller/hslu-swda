package ch.hslu.swda.persistence;

import ch.hslu.swda.entities.Store;
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
import java.util.NoSuchElementException;
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
        datastore.getMapper().map(DBOrder.class);
        datastore.getMapper().map(DBStore.class);
        datastore.ensureIndexes();
    }

    public void storeStore(Store store) {
        DBStore dbStore = StoreWrapper.createDBStore(store);
        datastore.save(dbStore);
    }

    public Store getStore(UUID storeId) throws NoSuchElementException {
        DBStore dbStore = datastore.find(DBStore.class)
                .filter(eq("_id", storeId))
                .first();
        if (dbStore == null) {
            LOG.info("No store detected for test case, we will return a default store");
            return Store.createExampleStore(storeId);
            //throw new NoSuchElementException("Store with the following id does not exist:" + storeId.toString());
        }
        return StoreWrapper.getStore(dbStore);
    }

    public List<Store> getAllStores() {
        List<Store> stores = new ArrayList<>();
        for (DBStore dbStore : datastore.find(DBStore.class)) {
            stores.add(StoreWrapper.getStore(dbStore));
        }
        return stores;
    }

}
