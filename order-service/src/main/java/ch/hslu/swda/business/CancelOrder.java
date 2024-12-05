package ch.hslu.swda.business;

import ch.hslu.swda.entities.Order;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.micro.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CancelOrder implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(CancelOrder.class);

    @Override
    public void modify(Order order, IngoingMessage responseRaw, Service service) {
        LOG.info("Order cacelled");
    }
}
