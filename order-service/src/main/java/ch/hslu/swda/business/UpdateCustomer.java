package ch.hslu.swda.business;

import ch.hslu.swda.entities.Order;
import ch.hslu.swda.messages.CustomerResponse;
import ch.hslu.swda.messages.IngoingMessage;
import ch.hslu.swda.messages.LogMessage;
import ch.hslu.swda.messages.VerifyResponse;
import ch.hslu.swda.micro.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class UpdateCustomer implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateCustomer.class);

    @Override
    public void modify(Order order, IngoingMessage responseRaw, Service service) {
        CustomerResponse response = (CustomerResponse) responseRaw;
        if (response.exists()) {
            order.getState().setCustomerReady(true);
            LOG.info("Order with the id {} has a valid customer", order.getId());
        } else {
            order.getState().setCancelled(true);
            LOG.info("Order with the id {} has not a valid customer", order.getId());
        }
    }
}
