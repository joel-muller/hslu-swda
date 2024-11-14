package ch.hslu.swda.business;

import ch.hslu.swda.entities.Order;

import java.io.IOException;

public interface Modifiable {
    void modify(Order order) throws IOException, InterruptedException;
}

