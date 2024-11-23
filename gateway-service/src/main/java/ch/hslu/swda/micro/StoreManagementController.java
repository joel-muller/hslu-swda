package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.RabbitMqConfig;
import ch.hslu.swda.model.Store;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.serde.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Controller for retrieving Store Management requests.
 */
@Tag(name = "StoreManagement")

@Controller("/api/v1/stores")
public class StoreManagementController {
    private static final Logger LOG = LoggerFactory.getLogger(StoreManagementController.class);
    private String exchangeName = new RabbitMqConfig().getExchange();
    private BusConnector bus = new BusConnector();
    @Inject
    private ObjectMapper mapper;

    @Post("/")
    public void receive(@Body Store store) {
        try {
            bus.connect();
            String reply = bus.talkSync(exchangeName, "store.create", mapper.writeValueAsString(store));
            if (reply == null) {
                LOG.info("received no empty reply from service");
            } else {
                LOG.info("received reply: " + reply);
            }
            ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
