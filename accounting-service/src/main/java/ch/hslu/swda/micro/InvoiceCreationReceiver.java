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

public class InvoiceCreationReceiver implements MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceCreationReceiver.class);
    private final String exchangeName;
    private final BusConnector bus;
    private DatabaseConnector database;

    public InvoiceCreationReceiver(String exchangeName, BusConnector bus, DatabaseConnector database) {
        this.exchangeName = exchangeName;
        this.bus = bus;
        this.database = database;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        try {
            LOG.debug("Received invoice creation request with replyTo [{}]", replyTo);
            ObjectMapper mapper = new ObjectMapper();
            InvoiceRequest invoiceRequest = mapper.readValue(message, InvoiceRequest.class);
            Invoice invoice = new Invoice(invoiceRequest);
            database.storeInvoice(invoice);
            LogEntry logEntry = new LogEntry("accounting-service",
                    Instant.now().getEpochSecond(),
                    UUID.randomUUID(),
                    "invoice.create",
                    invoice.getId(),
                    "Invoice created for orderId " + invoiceRequest.orderId() + " and customer "
                            + invoiceRequest.customerId());
            bus.talkAsync(exchangeName, "logs.new", mapper.writeValueAsString(logEntry));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
