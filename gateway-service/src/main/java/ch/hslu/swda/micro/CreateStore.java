package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.RabbitMqConfig;
import ch.hslu.swda.model.InternalOrderMessage;
import ch.hslu.swda.model.Store;
import ch.hslu.swda.model.StoreInventoryUpdate;
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
 * Controller for retrieving logs.
 */
@Tag(name = "createStore")

@Controller("/api/v1/createCreate")
public class CreateStore {
    private static final Logger LOG = LoggerFactory.getLogger(CreateStore.class);
    private String exchangeName = new RabbitMqConfig().getExchange();
    private BusConnector bus = new BusConnector();
    @Inject
    private ObjectMapper mapper;

    @Post("/")
    public void receive(@Body Store update) {
        try {
            bus.connect();
            String reply = bus.talkSync(exchangeName, "store.create-create", mapper.writeValueAsString(update));
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