package ch.hslu.swda.persistence;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;

import ch.hslu.swda.entities.Invoice;
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
    private final static String DATABASE_NAME = "accounting-database";

    public DatabaseConnector() {
        String mongoUri = System.getenv().getOrDefault("MONGO_URI", DATABASE_URI);
        LOG.info("Mongo URI is: {}", mongoUri);
        ConnectionString connectionString = new ConnectionString(mongoUri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .build();
        datastore = Morphia.createDatastore(MongoClients.create(settings), DATABASE_NAME);
        datastore.getMapper().map(Invoice.class);
        /* datastore.getMapper().map(DBOrder.class);
        datastore.getMapper().map(DBStore.class); */
        datastore.ensureIndexes();
    }



    public void storeInvoice(Invoice invoice) {
        //DBStore dbStore = StoreWrapper.createDBStore(invoice);
        datastore.save(invoice);
    }

    public Invoice getInvoiceFromInvoiceId(UUID invoiceId) throws NoSuchElementException {
        Invoice invoice = datastore.find(Invoice.class)
                .filter(eq("_id", invoiceId))
                .first();
        if (invoice == null) {
            LOG.info("Invoice not found in the database.");
            throw new NoSuchElementException("Invoice with the following id does not exist:" + invoiceId.toString());
        }
        //return StoreWrapper.getStore(dbStore);
        return invoice;
    }

        public Invoice getInvoiceFromCustomerId(UUID invoiceId) throws NoSuchElementException {
        Invoice invoice = datastore.find(Invoice.class)
                .filter(eq("_id", invoiceId))
                .first();
        if (invoice == null) {
            LOG.info("Invoice not found in the database.");
            throw new NoSuchElementException("Invoice with the following id does not exist:" + invoiceId.toString());
        }
        return invoice;
    }

    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        for (Invoice invoice : datastore.find(Invoice.class)) {
            invoices.add(invoice);
        }
        return invoices;
    }

}
