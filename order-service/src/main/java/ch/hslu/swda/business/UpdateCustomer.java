package ch.hslu.swda.business;

import ch.hslu.swda.entities.Order;
import ch.hslu.swda.messagesIngoing.CustomerResponse;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.micro.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateCustomer implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateCustomer.class);

    @Override
    public void modify(Order order, IngoingMessage responseRaw, Service service) {
        CustomerResponse response = (CustomerResponse) responseRaw;
        if (response.exists()) {
            order.getCopyOfState().setCustomerReady(true);
            LOG.info("Order with the id {} has a valid customer", order.getId());
        } else {
            order.getCopyOfState().setCancelled(true);
            LOG.info("Order with the id {} has not a valid customer", order.getId());
        }
    }
}
