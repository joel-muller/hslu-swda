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
import java.util.UUID;

import static dev.morphia.query.experimental.filters.Filters.eq;


public class DatabaseConnector implements Data {
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

    @Override
    public void storeStore(Store store) {
        DBStore dbStore = StoreWrapper.createDBStore(store);
        datastore.save(dbStore);
    }

    @Override
    public Store getStore(UUID storeId) {
        DBStore dbStore = datastore.find(DBStore.class)
                .filter(eq("_id", storeId))
                .first();
        if (dbStore == null) {
            return null;
        }
        return StoreWrapper.getStore(dbStore);
    }

    @Override
    public List<Store> getAllStores() {
        List<Store> stores = new ArrayList<>();
        for (DBStore dbStore : datastore.find(DBStore.class)) {
            stores.add(StoreWrapper.getStore(dbStore));
        }
        return stores;
    }

}
