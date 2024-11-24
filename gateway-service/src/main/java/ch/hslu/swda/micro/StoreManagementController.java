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
 * StoreManagementController is a REST controller that handles HTTP POST requests
 * for creating new store entries. It communicates with a message bus to send
 * store creation requests and logs the responses.
 * 
 * Annotations:
 * - @Tag: Specifies the name of the tag for this controller.
 * - @Controller: Maps HTTP requests to handler methods of this controller.
 * 
 * Dependencies:
 * - Logger: Used for logging information.
 * - RabbitMqConfig: Provides configuration for RabbitMQ exchange.
 * - BusConnector: Manages the connection and communication with the message bus.
 * - ObjectMapper: Used for converting Java objects to JSON and vice versa.
 * 
 * Methods:
 * - receive(Store store): Handles HTTP POST requests to create a new store. UUID for the store is not required as it will be assigned automatically.
 *   Connects to the message bus, sends the store creation request, and logs the response.
 * 
 * Exceptions:
 * - IOException: Thrown if an I/O error occurs during communication with the message bus.
 * - TimeoutException: Thrown if a timeout occurs while waiting for a response from the message bus.
 * - InterruptedException: Thrown if the thread is interrupted while waiting for a response from the message bus.
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
