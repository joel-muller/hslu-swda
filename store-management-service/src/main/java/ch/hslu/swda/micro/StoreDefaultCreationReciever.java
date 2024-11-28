package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.persistence.DatabaseConnector;
import ch.hslu.swda.entities.Store;
import ch.hslu.swda.messagesOutgoing.LogMessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class StoreDefaultCreationReciever implements MessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(StoreDefaultCreationReciever.class);

    private final DatabaseConnector database;
    private final StoreManagementService service;
    private final String exchangeName;
    private final BusConnector bus;

    public StoreDefaultCreationReciever(final DatabaseConnector database, final String exchangeName, final BusConnector bus,
            final StoreManagementService service) {
        this.exchangeName = exchangeName;
        this.bus = bus;
        this.database = database;
        this.service = service;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        try {
            LOG.info("Creating default stores...");
            for (int i = 0; i < 10; i++) {
                Store store = new Store();
                //TODO Artikelliste erstellen
                database.storeStore(store);
                LOG.info("Store with the id {} created and saved in the database", store.getId());
                service.log(new LogMessage(store.getId(), null, "store.create",
                        "Store Created: " + store.toString()));
            }
            bus.reply(exchangeName, replyTo, corrId, "Default Stores created.");

        } catch (IOException e) {
            LOG.error("Error occurred while creating the store object: {}", e.getMessage());
        }
    }
}
