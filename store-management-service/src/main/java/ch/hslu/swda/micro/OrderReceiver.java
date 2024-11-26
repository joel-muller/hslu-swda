package ch.hslu.swda.micro;

import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.business.DatabaseConnector;
import ch.hslu.swda.business.ProcessOrderStore;
import ch.hslu.swda.entities.Order;
import ch.hslu.swda.entities.Store;
import ch.hslu.swda.messages.OrderRequest;
import ch.hslu.swda.messages.OrderUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

public class OrderReceiver implements MessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(OrderReceiver.class);

    private final DatabaseConnector database;
    private final Service service;

    public OrderReceiver(final DatabaseConnector database, final Service service) {
        this.database = database;
        this.service = service;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            LOG.info(message);
            OrderRequest request = mapper.readValue(message, OrderRequest.class);
            Order order = Order.createFromOrderRequest(request);
            Store store = database.getStoreById(order.getStoreId());
            if (store == null) {
                store = Store.createExampleStore(order.getStoreId());
                store.modify(new ProcessOrderStore(this.service, order));
                //service.sendOrderUpdate(new OrderUpdate(order.getId(), new ArrayList<Integer>(), false));
            } else {
                store.modify(new ProcessOrderStore(this.service, order));
            }
            database.storeOrder(order);
            database.saveStoreObject(store);
            LOG.info("Order with the id {} stored", request.orderId());
        } catch (IOException e) {
            LOG.error("Error occurred while storing the order data data: {}", e.getMessage());
        }
    }
}
