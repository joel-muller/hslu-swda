package ch.hslu.swda.messagesIngoing;

import java.util.UUID;

public record OrderCancel(UUID orderId) implements IngoingMessage{
    @Override
    public UUID getOrderId() {
        return orderId;
    }
}
