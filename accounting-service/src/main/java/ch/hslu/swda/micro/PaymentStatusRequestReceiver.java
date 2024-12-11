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

/*Class to receive requests to check for outstanding payments from the gateway (synchronous communication). */
public class PaymentStatusRequestReceiver implements MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentStatusRequestReceiver.class);
    private final String exchangeName;
    private final BusConnector bus;
    private DatabaseConnector database;

    public PaymentStatusRequestReceiver(String exchangeName, BusConnector bus, DatabaseConnector database) {
        this.exchangeName = exchangeName;
        this.bus = bus;
        this.database = database;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        try {
            LOG.debug("Received payment status request with replyTo [{}]", replyTo);
            ObjectMapper mapper = new ObjectMapper();
            UUID customerId;
            try {
                customerId = UUID.fromString(message);
            } catch (IllegalArgumentException e) {
                customerId = reformatUUID(message);
            }

            Invoice invoice = database.getInvoiceFromCustomerId(customerId);
            Boolean paymentOverdue = invoice.getPaymentOverdue();
            String reply;
            if (paymentOverdue) {
                reply = "The customer has outstanding invoices and is not allowed to place new orders.";
            } else {
                reply = "The customer does not have any outstanding invoices.";
            }
            LogEntry logEntry = new LogEntry("accounting-service",
                    Instant.now().getEpochSecond(),
                    UUID.randomUUID(),
                    "paymentstatus.get",
                    invoice.getId(),
                    "Customer Payment status sent to gateway. " + invoice.toString());
            bus.talkAsync(exchangeName, "logs.new", mapper.writeValueAsString(logEntry));
            bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(reply));
            LOG.debug("Sent payment status to gateway with corrId [{}]", corrId);
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
