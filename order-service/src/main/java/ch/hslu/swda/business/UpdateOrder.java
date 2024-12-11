package ch.hslu.swda.business;

import ch.hslu.swda.entities.Order;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.messagesIngoing.OrderUpdate;
import ch.hslu.swda.messagesOutgoing.LogMessage;
import ch.hslu.swda.micro.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class UpdateOrder implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateOrder.class);

    @Override
    public void modify(Order order, IngoingMessage responseRaw, Service service) {
        OrderUpdate update = (OrderUpdate) responseRaw;
        LOG.info("Order update did go in to the order service {}", update.toString());
        order.handleOrderUpdate(update);
        try {
            if (order.isReady()) {
                service.log(new LogMessage(order.getId(), order.getEmployeeId(), "order.is Ready", "order with the id " + order.getId().toString() + " is now ready"));
                service.sendOrderReadyToStore(order.getOrderReady(), order.getInvoice());
            }
            if (order.isCancelled()) {
                service.sendOrderCancelledToStore(order.getOrderCancelled());
            }
        } catch (IOException e) {
            LOG.error("An error occurred while trying to update the order {}", e.getMessage());
        }
    }
}
