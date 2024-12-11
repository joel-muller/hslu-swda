package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.IncomingMessages.InvoiceRequest;
import ch.hslu.swda.entities.Invoice;
import ch.hslu.swda.entities.LogEntry;
import ch.hslu.swda.entities.SimpleOrder;
import ch.hslu.swda.persistence.DatabaseConnector;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

/*Class to receive requests to send existing invoice from the gateway (synchronous communication). */
public class GetInvoiceReceiver implements MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(GetInvoiceReceiver.class);
    private final String exchangeName;
    private final BusConnector bus;
    private DatabaseConnector database;

    public GetInvoiceReceiver(String exchangeName, BusConnector bus, DatabaseConnector database) {
        this.exchangeName = exchangeName;
        this.bus = bus;
        this.database = database;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        try {
            LOG.debug("Received invoice send request with replyTo [{}]", replyTo);
            ObjectMapper mapper = new ObjectMapper();
            UUID customerId = mapper.readValue(message, UUID.class);
/*             String customerIdStr = mapper.readValue(message, String.class);
            UUID customerId = UUID.fromString(customerIdStr); */
            Invoice invoice = database.getInvoiceFromCustomerId(customerId);
            LogEntry logEntry = new LogEntry("accounting-service",
                    Instant.now().getEpochSecond(),
                    UUID.randomUUID(),
                    "invoice.get",
                    invoice.getId(),
                    "Invoice sent to gateway. " + invoice.toString());
            bus.talkAsync(exchangeName, "logs.new", mapper.writeValueAsString(logEntry));
            bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(invoice));
            LOG.debug("Sent invoice to gateway with corrId [{}]", corrId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
