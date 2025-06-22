package ch.hslu.swda.business;

import ch.hslu.swda.entities.Store;
import ch.hslu.swda.messagesIngoing.StoreOrderCancelled;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.messagesOutgoing.LogMessage;
import ch.hslu.swda.micro.Service;
import ch.hslu.swda.persistence.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HandleOrderCancelled implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(HandleOrderCancelled.class);
    @Override
    public void modify(Data databaseConnector, IngoingMessage responseRaw, Service service) {
        try {
            StoreOrderCancelled response = (StoreOrderCancelled) responseRaw;
            Store store = databaseConnector.getStore(response.getStoreId());
            if (store != null) {
                store.cancelOrder(response.orderId());
                service.log(new LogMessage(response.orderId(), store.getId(), "storemanagement.ordercancelled", "order with the id " + response.orderId().toString() + " is cancelled and will get deleted in this service"));
                LOG.info("Order cancelled with the id {}, articles reserved from order are now part of the order inventory", response.orderId());
                databaseConnector.storeStore(store);
            }
        } catch (IOException e) {
            LOG.error("An error occurred while trying to cancel the order {}", e.getMessage());
        }
    }
}
