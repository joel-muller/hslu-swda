package ch.hslu.swda.business;

import ch.hslu.swda.entities.Order;
import ch.hslu.swda.entities.Store;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.messagesIngoing.OrderReady;
import ch.hslu.swda.messagesOutgoing.LogMessage;
import ch.hslu.swda.micro.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ProcessOrderReady implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessOrderReady.class);

    @Override
    public void modify(Store store, IngoingMessage responseRaw, Service service, DatabaseConnector database) {
        try {
            OrderReady request = (OrderReady) responseRaw;
            Order order = database.getOrderById(request.orderId());
            order.setFinished(true);
            store.removeOrder(order.getId());
            database.storeOrder(order);
            service.log(new LogMessage(request.orderId(), request.orderId(), "order finished", "Order with the id " + request.orderId().toString() + " is shipped"));
            LOG.info("Order finalized id {}", request.orderId());
        } catch (IOException e) {
            LOG.error("Exception occurred while trying to update the order {}", e.getMessage());
        }
    }
}
