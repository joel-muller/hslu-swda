package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.business.Customers;
import ch.hslu.swda.business.CustomersDatabase;
import ch.hslu.swda.entities.Customer;
import ch.hslu.swda.entities.LogEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

public class CustomerDeleter implements MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerDeleter.class);
    private final String exchangeName;
    private final BusConnector bus;

    private final Customers customers = new CustomersDatabase();

    public CustomerDeleter(String exchangeName, BusConnector bus) {
        this.exchangeName = exchangeName;
        this.bus = bus;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        try {
            LOG.debug("Received validity request with replyTo [{}]", replyTo);
            ObjectMapper mapper = new ObjectMapper();
            UUID uuid = mapper.readValue(message, UUID.class);
            Customer storedCustomer = customers.getById(uuid);
            LogEntry logEntry;
            if (storedCustomer == null) {
                logEntry = new LogEntry("customer-management-service",
                        Instant.now().getEpochSecond(),
                        UUID.randomUUID(),
                        "customer.delete",
                        uuid,
                        "Non-existing customer of id " + uuid + " attempted to be deleted.");
            } else {
                logEntry = new LogEntry("customer-management-service",
                        Instant.now().getEpochSecond(),
                        UUID.randomUUID(),
                        "customer.delete",
                        uuid,
                        "Customer of id " + uuid + " was deleted.");
            }
            boolean deleteSuccess = customers.deleteCustomer(uuid);
            bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(deleteSuccess));
            bus.talkAsync(exchangeName, "logs.new", mapper.writeValueAsString(logEntry));
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }
}
