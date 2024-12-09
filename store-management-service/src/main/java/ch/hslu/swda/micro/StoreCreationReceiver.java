package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.messagesIngoing.CreateStore;
import ch.hslu.swda.persistence.DatabaseConnector;
import ch.hslu.swda.entities.Store;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class StoreCreationReceiver implements MessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(StoreCreationReceiver.class);

    private final DatabaseConnector database;
    private final StoreManagementService service;
    private final String exchangeName;
    private final BusConnector bus;

    public StoreCreationReceiver(final DatabaseConnector database, final String exchangeName, final BusConnector bus,
                                 final StoreManagementService service) {
        this.exchangeName = exchangeName;
        this.bus = bus;
        this.database = database;
        this.service = service;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            CreateStore response = mapper.readValue(message, CreateStore.class);
            Store store = new Store();
            if (response.addDefaultArticle()) {
                store.addDefaultInventory();
            }
            database.storeStore(store);
        } catch (IOException e) {
            LOG.error("Error occured while creating a new store: {}", e.getMessage());
        }
    }
}
