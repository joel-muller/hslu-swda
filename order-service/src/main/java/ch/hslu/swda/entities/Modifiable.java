package ch.hslu.swda.entities;

import java.io.IOException;

public interface Modifiable {
    void modify(Order order) throws IOException, InterruptedException;
}

