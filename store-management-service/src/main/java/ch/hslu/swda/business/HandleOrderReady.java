package ch.hslu.swda.business;

import ch.hslu.swda.entities.Store;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.messagesIngoing.StoreOrderReady;
import ch.hslu.swda.messagesOutgoing.LogMessage;
import ch.hslu.swda.micro.Service;
import ch.hslu.swda.persistence.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HandleOrderReady implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(HandleOrderReady.class);

    @Override
    public void modify(Data databaseConnector, IngoingMessage responseRaw, Service service) {
        try {
            StoreOrderReady response = (StoreOrderReady) responseRaw;
            Store store = databaseConnector.getStore(response.getStoreId());
            if (store != null) {
                store.removeOrder(response.orderId());
                service.log(new LogMessage(response.orderId(), response.orderId(), "order finished", "Order with the id " + response.orderId().toString() + " is shipped"));
                LOG.info("Order finalized id {}", response.orderId());
                service.log(new LogMessage(response.orderId(), store.getId(), "storemanagement.orderReady", "order with the id " + response.orderId().toString() + " is ready and will get deleted in this service"));
                databaseConnector.storeStore(store);
            }
        } catch (IOException e) {
            LOG.error("Exception occurred while trying to update the order {}", e.getMessage());
        }
    }
}
