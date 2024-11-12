package ch.hslu.swda.business;

import ch.hslu.swda.entities.Customer;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import dev.morphia.Morphia;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Query;
import dev.morphia.query.Sort;
import dev.morphia.query.experimental.updates.UpdateOperators;
import org.bson.UuidRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

import static dev.morphia.query.experimental.filters.Filters.eq;

public class CustomersDatabase implements Customers {

    private static final Logger LOG = LoggerFactory.getLogger(CustomersDatabase.class);
    private final Datastore datastore;

    public CustomersDatabase() {
        String mongoUri = System.getenv().getOrDefault("MONGO_URI", "mongodb://localhost:27017");
        ConnectionString connectionString = new ConnectionString(mongoUri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .build();
        datastore = Morphia.createDatastore(MongoClients.create(settings), "customers");
        datastore.getMapper().map(Customer.class);
        datastore.ensureIndexes();
    }

    public CustomersDatabase(MongoClientSettings settings) {
        datastore = Morphia.createDatastore(MongoClients.create(settings), "customers");
        datastore.getMapper().map(Customer.class);
        datastore.ensureIndexes();
    }

    @Override
    public boolean addCustomer(Customer customer) {
        datastore.save(customer);
        LOG.info("Customer saved: " + customer);
        return true;
    }

    @Override
    public Customer getById(UUID id) {
        Customer customer = datastore.find(Customer.class)
                .filter(eq("_id", id))
                .first();
        LOG.debug("Customer with id [{}] retrieved", id);
        return customer;
    }

    @Override
    public List<Customer> findByFirstname(String firstname) {
        List<Customer> customerList = datastore.find(Customer.class)
                .filter(eq("firstname", firstname))
                .iterator(new FindOptions())
                .toList();
        LOG.debug("List of size [{}] for firstname [{}] retrieved", customerList.size(), firstname);
        return customerList;
    }

    @Override
    public List<Customer> findByLastname(String lastname) {
        List<Customer> customerList = datastore.find(Customer.class)
                .filter(eq("lastname", lastname))
                .iterator(new FindOptions())
                .toList();
        LOG.debug("List of size [{}] for lastname [{}] retrieved", customerList.size(), lastname);
        return customerList;
    }

    @Override
    public List<Customer> getAll() {
        List<Customer> customerList = datastore.find(Customer.class)
                .iterator(new FindOptions())
                .toList();
        LOG.debug("List of size [{}] retrieved", customerList.size());
        return customerList;
    }

    @Override
    public boolean deleteCustomer(UUID id) {
        DeleteResult result = datastore.find(Customer.class)
                .filter(eq("_id", id))
                .delete(new DeleteOptions());
        LOG.info("Customer of id [{}] was deleted", id);
        return result.wasAcknowledged();
    }

    @Override
    public boolean updateFirstname(UUID id, String firstname) {
        UpdateResult result = datastore.find(Customer.class)
                .filter(eq("_id", id))
                .update(UpdateOperators.set("firstname", firstname))
                .execute();
        LOG.debug("Firstname of Customer with id [{}] was updated to [{}]", id, firstname);
        return result.wasAcknowledged();
    }

    @Override
    public boolean updateLastname(UUID id, String lastname) {
        UpdateResult result = datastore.find(Customer.class)
                .filter(eq("_id", id))
                .update(UpdateOperators.set("lastname", lastname))
                .execute();
        LOG.debug("Lastname of Customer with id [{}] was updated to [{}]", id, lastname);
        return result.wasAcknowledged();
    }

    @Override
    public boolean updateCustomer(UUID id, String firstname, String lastname) {
        boolean updatedFirstname = updateFirstname(id, firstname);
        if (!updatedFirstname) {
            return false;
        }
        boolean updatedLastname = updateLastname(id, lastname);
        return updatedLastname;
    }
}
