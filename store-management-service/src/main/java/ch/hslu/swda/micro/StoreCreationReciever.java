package ch.hslu.swda.micro;

import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.business.DatabaseConnector;
import ch.hslu.swda.entities.Order;
import ch.hslu.swda.entities.Store;
import ch.hslu.swda.messages.OrderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class StoreCreationReciever implements MessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(StoreCreationReciever.class);

    private final DatabaseConnector database;
    private final StoreManagementService service;

    public StoreCreationReciever(final DatabaseConnector database, final StoreManagementService service) {
        this.database = database;
        this.service = service;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            LOG.info(message);
            Store store = mapper.readValue(message, Store.class);
            database.saveStoreObject(store);
            LOG.info("Store with the id {} created and saved in the database", store.getId());

        } catch (IOException e) {
            LOG.error("Error occurred while creating the store object: {}", e.getMessage());
        }
    }
}
