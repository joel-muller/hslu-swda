package ch.hslu.swda.business;

import ch.hslu.swda.entities.Order;
import ch.hslu.swda.messagesIngoing.OrderCustomerValidity;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.messagesOutgoing.LogMessage;
import ch.hslu.swda.messagesOutgoing.StoreOrderCancelled;
import ch.hslu.swda.micro.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateCustomer implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateCustomer.class);

    @Override
    public void modify(Order order, IngoingMessage responseRaw, Service service) {
        OrderCustomerValidity response = (OrderCustomerValidity) responseRaw;
        try {
            if (response.exists()) {
                order.setCustomerValid();
                LOG.info("Order with the id {} has a valid customer", order.getId());
                service.log(new LogMessage(order.getId(), order.getEmployeeId(), "order.customer", "order with the id " + order.getId() + " has a valid customer"));
                if (order.isReady()) {
                    service.log(new LogMessage(order.getId(), order.getEmployeeId(), "order.is Ready", "order with the id " + order.getId().toString() + " is now ready"));
                    service.sendOrderReadyToStore(order.getOrderReady(), order.getInvoice());
                    service.log(new LogMessage(order.getId(), order.getEmployeeId(), "order.is Ready", "order invoice is sent for the order with the id " + order.getId().toString()));
                }
            } else {
                order.setCancelled();
                service.log(new LogMessage(order.getId(), order.getEmployeeId(), "order.customer", "order with the id " + order.getId() + " has a not valid customer and was cancelled"));
                service.sendOrderCancelledToStore(new StoreOrderCancelled(order.getId(), order.getStoreId()));
                LOG.info("Order with the id {} has not a valid customer", order.getId());
            }
        } catch (Exception e) {
            LOG.error("An exception occurred while trying to send further messages {}", e.toString());
        }

    }
}