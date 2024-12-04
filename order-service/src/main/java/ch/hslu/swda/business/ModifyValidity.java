package ch.hslu.swda.business;

import ch.hslu.swda.entities.Order;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.messagesOutgoing.LogMessage;
import ch.hslu.swda.messagesIngoing.VerifyResponse;
import ch.hslu.swda.micro.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ModifyValidity implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(ModifyValidity.class);

    @Override
    public void modify(Order order, IngoingMessage responseRaw, Service service) {
        try {
            VerifyResponse response = (VerifyResponse) responseRaw;
            order.handleVerifyResponse(response);
            if (!order.isCancelled()) {
                service.log(new LogMessage(order.getId(), order.getEmployeeId(), "order.validate", "Order Validated, order id: " + order.getId().toString()));
                service.requestArticlesFromStore(order.getStoreRequest());
                service.checkCustomerValidity(order.getCustomerRequest());
                LOG.info("Received Validity check oder with the id {} is Valid", order.getId());
            } else {
                order.getState().setCancelled(true);
                service.log(new LogMessage(order.getId(), order.getEmployeeId(), "order.validate", "Order not validated, order id: " + order.getId().toString()));
                LOG.info("Received Validity check oder with the id {} is not Valid", order.getId());
            }
        } catch (IOException e) {
            LOG.error("An error occurred while trying to call further actions after validity was arrived: {}", e.toString());
        }
    }
}
