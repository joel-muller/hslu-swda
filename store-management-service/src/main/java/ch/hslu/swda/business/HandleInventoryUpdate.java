package ch.hslu.swda.business;

import ch.hslu.swda.entities.Order;
import ch.hslu.swda.entities.OrderProcessed;
import ch.hslu.swda.entities.Store;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.messagesIngoing.InventoryUpdate;
import ch.hslu.swda.messagesIngoing.NewOrder;
import ch.hslu.swda.messagesOutgoing.InventoryRequest;
import ch.hslu.swda.messagesOutgoing.OrderUpdate;
import ch.hslu.swda.micro.Service;
import ch.hslu.swda.persistence.Data;
import ch.hslu.swda.persistence.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class HandleInventoryUpdate implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(HandleInventoryUpdate.class);


    @Override
    public void modify(Data databaseConnector, IngoingMessage responseRaw, Service service) {
        try {
            InventoryUpdate request = (InventoryUpdate) responseRaw;
            Store store = databaseConnector.getStore(request.getStoreId());
            if (store == null) {
                return;
            }
            OrderProcessed processed = store.handleInventoryUpdate(request.orderId(), request.articles());
            if (!processed.articlesReady().isEmpty()) {
                service.sendOrderUpdate(new OrderUpdate(processed.orderId(), processed.articlesReady(), true));
            }
            // The article which should be ordered are already ordered in the handle new order
            databaseConnector.storeStore(store);
            LOG.info("Inventory update received and stored successfully for the store {}", request.getStoreId());
        } catch (IOException e) {
            LOG.error("Exception occurred while trying to make an inventory update {}", e.getMessage());
        }
    }
}
