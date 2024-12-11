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
import java.util.List;
import java.util.UUID;

/*Class to receive requests to send existing invoice from the gateway (synchronous communication). */
public class GetAllInvoicesReceiver implements MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllInvoicesReceiver.class);
    private final String exchangeName;
    private final BusConnector bus;
    private DatabaseConnector database;

    public GetAllInvoicesReceiver(String exchangeName, BusConnector bus, DatabaseConnector database) {
        this.exchangeName = exchangeName;
        this.bus = bus;
        this.database = database;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            LOG.debug("Received invoices send request with replyTo [{}]", replyTo);
            List<Invoice> invoices = database.getAllInvoices();
            String invoiceListJson = mapper.writeValueAsString(invoices);
            LogEntry logEntry = new LogEntry("accounting-service",
                    Instant.now().getEpochSecond(),
                    UUID.randomUUID(),
                    "invoices.get",
                    UUID.randomUUID(),
                    "Invoices sent to gateway. ");
            bus.talkAsync(exchangeName, "logs.new", mapper.writeValueAsString(logEntry));
            bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(invoiceListJson));
            LOG.debug("Sent invoice to gateway with corrId [{}]", corrId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
