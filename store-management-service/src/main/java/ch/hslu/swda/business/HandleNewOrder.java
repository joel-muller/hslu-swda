package ch.hslu.swda.business;

import ch.hslu.swda.entities.*;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.messagesIngoing.OrderRequest;
import ch.hslu.swda.messagesOutgoing.InventoryRequest;
import ch.hslu.swda.messagesOutgoing.OrderUpdate;
import ch.hslu.swda.micro.Service;
import ch.hslu.swda.persistence.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HandleNewOrder implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(HandleNewOrder.class);

    @Override
    public void modify(DatabaseConnector databaseConnector, IngoingMessage responseRaw, Service service) {
        try {
            LOG.info("Handle new order");
            OrderRequest request = (OrderRequest) responseRaw;
            Store store = databaseConnector.getStore(request.getStoreId());
            OrderProcessed processed = store.newOrder(request);
            if (!processed.articlesHaveToGetOrdered().isEmpty()) {
                service.requestArticles(new InventoryRequest(request.orderId(), request.storeId(), processed.articlesHaveToGetOrdered()));
            }
            if (!processed.articlesReady().isEmpty()) {
                service.sendOrderUpdate(new OrderUpdate(request.orderId(), processed.articlesReady()));
            }
            databaseConnector.storeStore(store);
            LOG.info("New order {} arrived for the store {}", request.orderId(), request.storeId());
        } catch (IOException e) {
            LOG.error("Exception occurred while trying to update the order {}", e.getMessage());
        }
//        catch (NoSuchElementException e) {
//            //TBD for when a store does not exist
//        }
    }
}
