package ch.hslu.swda.micro;

import ch.hslu.swda.messagesOutgoing.WarehouseRequest;
import ch.hslu.swda.messagesOutgoing.LogMessage;
import ch.hslu.swda.messagesOutgoing.OrderUpdate;

import java.io.IOException;

public interface Service {
    void log(LogMessage message) throws IOException;
    void sendOrderUpdate(OrderUpdate update) throws IOException;
    void requestArticles(WarehouseRequest request) throws IOException;
}
