package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.business.Customers;
import ch.hslu.swda.business.CustomersDatabase;
import ch.hslu.swda.entities.Customer;
import ch.hslu.swda.entities.ValidityRequest;
import ch.hslu.swda.entities.ValidityResponse;
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
            ValidityRequest request = mapper.readValue(message, ValidityRequest.class);
            Customer customer = customers.getById(request.customerId());
            boolean retVal = customer != null;
            LOG.debug("Customer of id [{}] existing: [{}]", request.customerId(), retVal);
            ValidityResponse response = new ValidityResponse(request.customerId(), retVal);
            bus.talkAsync(exchangeName, "order.customer-validity", mapper.writeValueAsString(response));
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }
}
