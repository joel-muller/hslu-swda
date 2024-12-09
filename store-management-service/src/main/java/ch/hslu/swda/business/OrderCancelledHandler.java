package ch.hslu.swda.business;

import ch.hslu.swda.entities.Store;
import ch.hslu.swda.messagesIngoing.CancelOrder;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.messagesIngoing.OrderReady;
import ch.hslu.swda.messagesOutgoing.LogMessage;
import ch.hslu.swda.micro.Service;
import ch.hslu.swda.persistence.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class OrderCancelledHandler implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(OrderCancelledHandler.class);
    @Override
    public void modify(DatabaseConnector databaseConnector, IngoingMessage responseRaw, Service service) {
        CancelOrder response = (CancelOrder) responseRaw;
        Store store = databaseConnector.getStore(response.getStoreId());
        store.cancelOrder(response.orderId());
        LOG.info("Order cancelled with the id {}, articles reserved from order are now part of the order inventory", response.orderId());
        databaseConnector.storeStore(store);
    }
}
