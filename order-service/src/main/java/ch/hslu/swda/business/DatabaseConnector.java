package ch.hslu.swda.business;

import ch.hslu.swda.entities.Article;
import ch.hslu.swda.entities.Order;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import org.bson.UuidRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static dev.morphia.query.experimental.filters.Filters.eq;

public class DatabaseConnector {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseConnector.class);

    private final Datastore datastore;

    private final static String DATABASE_URI = "mongodb://localhost:27017";
    private final static String DATABASE_NAME = "orders";

    public DatabaseConnector() {
        String mongoUri = System.getenv().getOrDefault("MONGO_URI", DATABASE_URI);
        ConnectionString connectionString = new ConnectionString(mongoUri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .build();
        datastore = Morphia.createDatastore(MongoClients.create(settings), DATABASE_NAME);
        datastore.getMapper().map(Order.class);
        datastore.ensureIndexes();
    }

    public void storeOrder(Order order) {
        datastore.save(order);
        LOG.info("Order stored: " + order);
    }

    public void storeArticle(Article article) {
        datastore.save(article);
        LOG.info("Article stored: " + article);
    }

    public Order getById(UUID id) {
        Order order = datastore.find(Order.class)
                .filter(eq("_id", id))
                .first();
        return order;
    }
}
