package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.IncomingMessages.InvoiceRequest;
import ch.hslu.swda.entities.Invoice;
import ch.hslu.swda.entities.LogEntry;
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
            LOG.info("received invoice get request with message: " + message);
            ObjectMapper mapper = new ObjectMapper();
            UUID customerId;
            try {
                customerId = UUID.fromString(message);
            } catch (IllegalArgumentException e) {
                customerId = reformatUUID(message);
            }

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

    private UUID reformatUUID(String uuidStr) {
        // Validate the length of the UUID string
        if (uuidStr.length() != 32) {
            throw new IllegalArgumentException("Invalid UUID string length: " + uuidStr.length());
        }

        // Reformat the UUID string to include hyphens
        String formattedUUID = uuidStr.replaceFirst(
                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                "$1-$2-$3-$4-$5");
        return UUID.fromString(formattedUUID);
    }
}
