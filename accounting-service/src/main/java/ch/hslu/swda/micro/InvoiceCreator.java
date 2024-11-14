package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.entities.LogEntry;
import ch.hslu.swda.entities.SimpleOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

public class InvoiceCreator implements MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceCreator.class);
    private final String exchangeName;
    private final BusConnector bus;

    public InvoiceCreator(String exchangeName, BusConnector bus) {
        this.exchangeName = exchangeName;
        this.bus = bus;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        try {
            LOG.debug("Received get request with replyTo [{}]", replyTo);
            ObjectMapper mapper = new ObjectMapper();
            SimpleOrder order = mapper.readValue(message, SimpleOrder.class);
            LogEntry logEntry = new LogEntry("accounting-service",
                    Instant.now().getEpochSecond(),
                    UUID.randomUUID(),
                    "invoice.create",
                    order.orderId(),
                    "Invoice created for orderId " + order.orderId() + " and customer " + order.customerId() + ".");
            // TODO send order invoice back to service
            bus.talkAsync(exchangeName, "logs.new", mapper.writeValueAsString(logEntry));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
