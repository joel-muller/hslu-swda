package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.RabbitMqConfig;
import ch.hslu.swda.model.Store;
import ch.hslu.swda.model.StoreInventoryUpdate;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.serde.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


@Tag(name = "Invoice")

@Controller("/api/v1/invoices")
public class InvoiceController {
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceController.class);
    private String exchangeName = new RabbitMqConfig().getExchange();
    private BusConnector bus = new BusConnector();
    @Inject
    private ObjectMapper mapper;

    /* receive all invoices from the system. */
    @Get("/")
    public String getInvoices() {
        try {
            bus.connect();
            String reply = bus.talkSync(exchangeName, "invoices.get", "");
            if (reply == null) {
                LOG.info("received empty reply from service");
                return "no reply from service";
            } else {
                LOG.info("received reply: " + reply);
                return reply;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /* Receive the invoice according to a customer or invoice UUID. */
    @Get("/{id}")
    public String getInvoiceFromID(String id) {
        try {
            bus.connect();
            String reply = bus.talkSync(exchangeName, "invoice.get", id);
            if (reply == null) {
                LOG.info("received empty reply from service");
                return "no reply from service";
            } else {
                LOG.info("received reply: " + reply);
                return reply;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
