package ch.hslu.swda.business;

import ch.hslu.swda.entities.*;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.messagesIngoing.StoreRequestArticles;
import ch.hslu.swda.messagesOutgoing.WarehouseRequest;
import ch.hslu.swda.messagesOutgoing.LogMessage;
import ch.hslu.swda.messagesOutgoing.OrderUpdate;
import ch.hslu.swda.micro.Service;
import ch.hslu.swda.persistence.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

public class HandleNewOrder implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(HandleNewOrder.class);

    @Override
    public void modify(Data databaseConnector, IngoingMessage responseRaw, Service service) {
        try {
            StoreRequestArticles request = (StoreRequestArticles) responseRaw;
            Store store = databaseConnector.getStore(request.getStoreId());
            if (store == null) {
                service.sendOrderUpdate(new OrderUpdate(request.orderId(), new ArrayList<>(), false));
                return;
            }
            service.log(new LogMessage(request.orderId(), request.storeId(), "storemanagement.newOrder", "a new order arrived at the store with the id " + store.getId().toString()));
            OrderProcessed processed = store.newOrder(request.orderId(), request.articles());
            if (!processed.articlesHaveToGetOrdered().isEmpty()) {
                service.log(new LogMessage(request.orderId(), request.storeId(), "storemangement.centralwarehouseOrder", "following articles have to get ordered from central warehouse" + processed.articlesHaveToGetOrdered().toString()));
                service.requestArticles(new WarehouseRequest(request.orderId(), request.storeId(), processed.articlesHaveToGetOrdered()));
            }
            if (!processed.articlesReady().isEmpty()) {
                service.log(new LogMessage(request.orderId(), request.storeId(), "storemangement.articlesinstore", "following articles are in store" + processed.articlesReady().toString()));
                service.sendOrderUpdate(new OrderUpdate(request.orderId(), processed.articlesReady(), true));
            }
            databaseConnector.storeStore(store);
            LOG.info("New order {} arrived for the store {}", request.orderId(), request.storeId());
        } catch (IOException e) {
            LOG.error("Exception occurred while trying to update the order {}", e.getMessage());
        }
    }
}
