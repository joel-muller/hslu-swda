package ch.hslu.swda.business;

import ch.hslu.swda.entities.Order;

import java.io.IOException;

/**
 * A strategy for modifying an order
 */
public interface Modifiable {
    /**
     * Modifies an order
     * @param order the order
     */
    void modify(Order order);
}

