package ch.hslu.swda.business;

import ch.hslu.swda.entities.Store;
import ch.hslu.swda.messagesIngoing.OrderCancelled;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.micro.Service;
import ch.hslu.swda.persistence.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandleOrderCancelled implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(HandleOrderCancelled.class);
    @Override
    public void modify(DatabaseConnector databaseConnector, IngoingMessage responseRaw, Service service) {
        OrderCancelled response = (OrderCancelled) responseRaw;
        Store store = databaseConnector.getStore(response.getStoreId());
        store.cancelOrder(response.orderId());
        LOG.info("Order cancelled with the id {}, articles reserved from order are now part of the order inventory", response.orderId());
        databaseConnector.storeStore(store);
    }
}
