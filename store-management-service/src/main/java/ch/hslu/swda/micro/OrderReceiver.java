package ch.hslu.swda.micro;

import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.business.DatabaseConnector;
import ch.hslu.swda.entities.Order;
import ch.hslu.swda.messages.OrderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class OrderReceiver implements MessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(OrderReceiver.class);

    private final DatabaseConnector database;
    private final StoreManagementService service;

    public OrderReceiver(final DatabaseConnector database, final StoreManagementService service) {
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
            database.storeOrder(order);
            LOG.info("Order with the id {} stored", request.orderId());
        } catch (IOException e) {
            LOG.error("Error occurred while storing the order data data: {}", e.getMessage());
        }
    }
}
