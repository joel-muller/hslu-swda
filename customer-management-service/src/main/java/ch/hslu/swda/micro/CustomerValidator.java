package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.business.Customers;
import ch.hslu.swda.business.CustomersDatabase;
import ch.hslu.swda.entities.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class CustomerValidator implements MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerValidator.class);
    private final String exchangeName;
    private final BusConnector bus;

    private final Customers customers = new CustomersDatabase();

    public CustomerValidator(String exchangeName, BusConnector bus) {
        this.exchangeName = exchangeName;
        this.bus = bus;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        try {
            LOG.debug("Received validity request with replyTo [{}]", replyTo);
            ObjectMapper mapper = new ObjectMapper();
            UUID id = mapper.readValue(message, UUID.class);
            Customer customer = customers.getById(id);
            boolean retVal = customer != null;
            LOG.debug("Customer of id [{}] existing: [{}]", id, retVal);
            bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(retVal));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
