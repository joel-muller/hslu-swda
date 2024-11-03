package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.entities.StoreManagementDB;

import java.io.IOException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InventoryCheckReceiver implements ch.hslu.swda.bus.MessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(InventoryCheckReceiver.class);
    private final String exchangeName;
    private final BusConnector bus;
    private StoreManagementDB db;

    public InventoryCheckReceiver(final String exchangeName, final BusConnector bus, final StoreManagementDB db) {
        this.exchangeName = exchangeName;
        this.bus = bus;
        this.db = db;
    }

    @Override
    public void onMessageReceived(final String route, final String replyTo, final String corrId, final String message) {
        // log event
        String threadName = Thread.currentThread().getName();
        LOG.debug("[Thread: {}] Begin message processing", threadName);
        LOG.debug("Received message with routing [{}]", route);

        // unpack received message data
        ObjectMapper mapper = new ObjectMapper();
        /* TypeReference<Order> typeRef = new TypeReference<>() {
            // empty
        }; */
        try {
            // process message data
            Integer article = mapper.readValue(message, Integer.class);
            db.checkArticleAvailabilityAsJson(article);
        } catch (IOException | SQLException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            LOG.debug("[Thread: {}] End message processing", threadName);
        }
    }


    
}
