package ch.hslu.swda.business;

import ch.hslu.swda.messagesOutgoing.InventoryRequest;
import ch.hslu.swda.messagesOutgoing.LogMessage;
import ch.hslu.swda.messagesOutgoing.OrderUpdate;
import ch.hslu.swda.micro.Service;

import java.io.IOException;

public class FakeService implements Service {
    LogMessage lastLogMessage;
    OrderUpdate lastOrderUpdate;
    InventoryRequest lastInventoryRequest;

    @Override
    public void log(LogMessage message) throws IOException {
        this.lastLogMessage = message;
    }

    @Override
    public void sendOrderUpdate(OrderUpdate update) throws IOException {
        this.lastOrderUpdate = update;
    }

    @Override
    public void requestArticles(InventoryRequest request) throws IOException {
        this.lastInventoryRequest = request;
    }
}
