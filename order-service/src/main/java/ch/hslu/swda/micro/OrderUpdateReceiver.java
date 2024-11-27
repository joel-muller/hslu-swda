package ch.hslu.swda.micro;

import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.business.DatabaseConnector;
import ch.hslu.swda.business.ModifyValidity;
import ch.hslu.swda.business.UpdateOrder;
import ch.hslu.swda.entities.Order;
import ch.hslu.swda.messages.OrderUpdate;
import ch.hslu.swda.messages.VerifyResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class OrderUpdateReceiver implements MessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(OrderUpdateReceiver.class);
    private final DatabaseConnector database;
    private final OrderService service;

    public OrderUpdateReceiver(final DatabaseConnector database, final OrderService service) {
        this.database = database;
        this.service = service;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            OrderUpdate response = mapper.readValue(message, OrderUpdate.class);
            Order order = database.getById(response.id());
            order.modify(new UpdateOrder(response, this.service));
            database.storeOrder(order);
            LOG.info("Received order Update check and order was updated: [{}]", response.toString());
        } catch (IOException e) {
            LOG.error("Error occurred while mapping the validity reception data: {}", e.getMessage());
        }

        LOG.debug("received chat message with replyTo property [{}]: [{}]", replyTo, message);
        LOG.debug("sending answer with topic [{}] according to replyTo-property", replyTo);
    }
}
