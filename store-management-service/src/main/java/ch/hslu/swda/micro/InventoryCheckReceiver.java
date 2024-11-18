package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;

import java.io.IOException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class InventoryCheckReceiver implements ch.hslu.swda.bus.MessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(InventoryCheckReceiver.class);
    private final String exchangeName;
    private final BusConnector bus;
    ObjectMapper mapper = new ObjectMapper();

    public InventoryCheckReceiver(final String exchangeName, final BusConnector bus) {
        this.exchangeName = exchangeName;
        this.bus = bus;
    }

    @Override
    public void onMessageReceived(final String route, final String replyTo, final String corrId, final String message) {
        // log event

        LOG.debug("Received message with routing [{}]", route);

        try {
            // process message data
            ObjectMapper mapper = new ObjectMapper();
            Integer article = mapper.readValue(message, Integer.class);
            bus.talkAsync(exchangeName, replyTo, "Hello"); // asynchron beantworten
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            LOG.debug("[Ended message processing of inventory check request with message [{}]]", message);
        }
    }

}
