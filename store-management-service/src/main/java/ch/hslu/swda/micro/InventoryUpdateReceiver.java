package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.business.InventoryUpdateStore;
import ch.hslu.swda.business.Modifiable;
import ch.hslu.swda.entities.Store;
import ch.hslu.swda.messagesIngoing.InventoryUpdate;
import ch.hslu.swda.messagesOutgoing.LogMessage;
import ch.hslu.swda.persistence.DatabaseConnector;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class InventoryUpdateReceiver implements MessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(InventoryUpdateReceiver.class);

    private final DatabaseConnector database;
    private final StoreManagementService service;
    private final String exchangeName;
    private final BusConnector bus;

    public InventoryUpdateReceiver(final DatabaseConnector database, final String exchangeName, final BusConnector bus,
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
            InventoryUpdate update = mapper.readValue(message, InventoryUpdate.class);

            LOG.info("Inventory update received {}", update.toString());
//            Modifiable mod = new InventoryUpdateStore();
//            mod.modify(database, update, service);

            bus.reply(exchangeName, replyTo, corrId, "Store created" + update.toString());

        } catch (IOException e) {
            LOG.error("Error occurred while creating the store object: {}", e.getMessage());
        }
    }
}
