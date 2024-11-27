package ch.hslu.swda.business;

import ch.hslu.swda.entities.Order;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.micro.Service;

/**
 * A strategy for modifying an order
 */
public interface Modifiable {
    /**
     * Modifies an order
     * @param order the order
     */
    void modify(Order order, IngoingMessage responseRaw, Service service);
}

