package ch.hslu.swda.micro;

import ch.hslu.swda.messages.LogMessage;
import ch.hslu.swda.messages.OrderUpdate;

import java.io.IOException;

public interface Service {
    void log(LogMessage message) throws IOException;
    void sendOrderUpdate(OrderUpdate update) throws IOException;
}
