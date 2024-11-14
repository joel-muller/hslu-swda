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

public class CustomerUpdater implements MessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerUpdater.class);
    private final String exchangeName;
    private final BusConnector bus;

    private final Customers customers = new CustomersDatabase();

    public CustomerUpdater(String exchangeName, BusConnector bus) {
        this.exchangeName = exchangeName;
        this.bus = bus;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        try {
            LOG.debug("Received update request with replyTo [{}]", replyTo);
            ObjectMapper mapper = new ObjectMapper();
            Customer receivedCustomer = mapper.readValue(message, Customer.class);
            LOG.info(receivedCustomer.getFirstname());
            LOG.info(receivedCustomer.getLastname());
            Customer storedCustomer = customers.getById(receivedCustomer.getId());
            if (storedCustomer == null) {
                bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(false));
                LOG.info("Customer of id [{}] does not exist.", receivedCustomer.getId());
                return;
            }
            boolean updateSuccess = true;
            if (!receivedCustomer.getFirstname().isEmpty() && !storedCustomer.getFirstname().equals(receivedCustomer.getFirstname())) {
                updateSuccess = customers.updateFirstname(
                        receivedCustomer.getId(),
                        receivedCustomer.getFirstname()
                );
            }
            if (!updateSuccess) {
                bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(false));
                LOG.info("Firstname of customer of id [{}] could not be updated to [{}].", receivedCustomer.getId(), receivedCustomer.getFirstname());
                return;
            }
            if (!receivedCustomer.getLastname().isEmpty() && !storedCustomer.getLastname().equals(receivedCustomer.getLastname())) {
                updateSuccess = customers.updateLastname(
                        receivedCustomer.getId(),
                        receivedCustomer.getLastname()
                );
            }
            if (!updateSuccess) {
                bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(false));
                LOG.info("Firstname of customer of id [{}] could not be updated to [{}].", receivedCustomer.getId(), receivedCustomer.getFirstname());
                return;
            }
            LogEntry logEntry = new LogEntry("customer-management-service",
                    Instant.now().getEpochSecond(),
                    UUID.randomUUID(),
                    "customer.update",
                    receivedCustomer.getId(),
                    "Customer data was updated. Current state: " + receivedCustomer);
            bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(true));
            LOG.debug("Customer of id [{}] successfully updated.", receivedCustomer.getId());
            bus.talkAsync(exchangeName, "logs.new", mapper.writeValueAsString(logEntry));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
