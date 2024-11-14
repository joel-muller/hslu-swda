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

public class CustomerRetriever implements MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerRetriever.class);
    private final String exchangeName;
    private final BusConnector bus;

    private final Customers customers = new CustomersDatabase();

    public CustomerRetriever(String exchangeName, BusConnector bus) {
        this.exchangeName = exchangeName;
        this.bus = bus;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        try {

            LOG.debug("Received get request with replyTo [{}]", replyTo);
            ObjectMapper mapper = new ObjectMapper();
            if (!message.isEmpty()) {
                UUID uuid = mapper.readValue(message, UUID.class);
                Customer customer = customers.getById(uuid);
                bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(customer));
            } else {
                List<Customer> list = customers.getAll();
                LOG.debug("Sending list of log entries with size [{}]", list.size());
                bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(list));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
