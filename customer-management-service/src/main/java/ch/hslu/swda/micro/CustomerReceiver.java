package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.business.Customers;
import ch.hslu.swda.business.CustomersDatabase;
import ch.hslu.swda.entities.Customer;
import ch.hslu.swda.entities.LogEntry;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

public class CustomerReceiver implements MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerReceiver.class);
    private final String exchangeName;
    private final BusConnector bus;

    private final Customers customers = new CustomersDatabase();

    public CustomerReceiver(String exchangeName, BusConnector bus) {
        this.exchangeName = exchangeName;
        this.bus = bus;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        LOG.debug("Received message with routing [{}]", route);

        ObjectMapper mapper = new ObjectMapper();

        TypeReference<Customer> typeRef = new TypeReference<>() {};

        try {
            Customer customer = mapper.readValue(message, typeRef);
            if (customer.getId() != null) {
                customer = new Customer(customer.getFirstname(), customer.getLastname());
                customers.addCustomer(customer);
            } else {
                customers.addCustomer(customer);
            }
            String logMessage = "Customer " + customer.getFirstname() + " " + customer.getLastname() + " with id " + customer.getId() + " created.";
            LogEntry log = new LogEntry("customer-management-service",
                    Instant.now().getEpochSecond(),
                    UUID.randomUUID(),
                    "customer.create",
                    customer.getId(),
                    logMessage);
            bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(customer));
            bus.talkAsync(exchangeName, "logs.new", mapper.writeValueAsString(log));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
