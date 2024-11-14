package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.entities.StoreManagementDB;

import java.io.IOException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class InventoryCheckReceiver implements ch.hslu.swda.bus.MessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(InventoryCheckReceiver.class);
    private final String exchangeName;
    private final BusConnector bus;
    private StoreManagementDB db;
    ObjectMapper mapper = new ObjectMapper();

    public InventoryCheckReceiver(final String exchangeName, final BusConnector bus, final StoreManagementDB db) {
        this.exchangeName = exchangeName;
        this.bus = bus;
        this.db = db;
    }

    @Override
    public void onMessageReceived(final String route, final String replyTo, final String corrId, final String message) {
        // log event

        LOG.debug("Received message with routing [{}]", route);

        try {
            // process message data
            ObjectMapper mapper = new ObjectMapper();
            Integer article = mapper.readValue(message, Integer.class);
            ArrayNode responseArrayNode = db.checkArticleAvailabilityAsJson(article);
            String responseString = responseArrayNode.toString();
            bus.talkAsync(exchangeName, replyTo, responseString); // asynchron beantworten
        } catch (IOException | SQLException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            LOG.debug("[Ended message processing of inventory check request with message [{}]]", message);
        }
    }

}
