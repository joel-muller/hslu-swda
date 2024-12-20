package ch.hslu.swda.business;

import ch.hslu.swda.entities.Order;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.messagesOutgoing.LogMessage;
import ch.hslu.swda.micro.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CancelOrder implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(CancelOrder.class);

    @Override
    public void modify(Order order, IngoingMessage responseRaw, Service service) {
        try {
            if (!order.isReady()) {
                order.setCancelled();
                service.sendOrderCancelledToStore(order.getOrderCancelled());
                service.log(new LogMessage(order.getId(), order.getEmployeeId(), "order.cancelled", "order with the id " + order.getId().toString() + " cancelled"));
                LOG.info("Order cancelled successfully");
            } else {
                LOG.info("Order could not get cancelled because it is already finished");
            }
        } catch (IOException e) {
            LOG.error("An error occurred while trying to cancel the order: {}", e.toString());
        }
    }
}
