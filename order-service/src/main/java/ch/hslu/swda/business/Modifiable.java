package ch.hslu.swda.business;

import ch.hslu.swda.entities.Order;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.micro.Service;

/**
 * A strategy for modifying an order
 */
public interface Modifiable {
    /**
     * Modify an order with a modifiable
     * @param order The order
     * @param responseRaw The message which arrived in the service (IngoingMessage is an interface and you can get orderId out of it)
     * @param service the service for sending further messages
     */
    void modify(Order order, IngoingMessage responseRaw, Service service);
}

