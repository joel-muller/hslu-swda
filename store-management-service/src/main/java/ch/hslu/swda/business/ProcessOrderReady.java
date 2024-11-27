package ch.hslu.swda.business;

import ch.hslu.swda.entities.ArticleOrdered;
import ch.hslu.swda.entities.Order;
import ch.hslu.swda.entities.Store;
import ch.hslu.swda.entities.StoreArticle;
import ch.hslu.swda.messages.LogMessage;
import ch.hslu.swda.messages.OrderReady;
import ch.hslu.swda.messages.OrderRequest;
import ch.hslu.swda.messages.OrderUpdate;
import ch.hslu.swda.micro.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProcessOrderReady implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessOrderReady.class);
    private final Service service;
    private final DatabaseConnector databaseConnector;
    private final OrderReady request;

    public ProcessOrderReady(Service service, DatabaseConnector databaseConnector, OrderReady request) {
        this.service = service;
        this.databaseConnector = databaseConnector;
        this.request = request;
    }

    @Override
    public void modify(Store store) {
        try {
            Order order = databaseConnector.getOrderById(request.orderId());
            order.setFinished(true);
            store.getOpenOrders().remove(request.orderId());
            databaseConnector.storeOrder(order);
            service.log(new LogMessage(request.orderId(), request.orderId(), "order finished", "Order with the id " + request.orderId().toString() + " is shipped"));
            LOG.info("Order finalized id {}", request.orderId());
        } catch (IOException e) {
            LOG.error("Exception occurred while trying to update the order {}", e.getMessage());
        }
    }
}
