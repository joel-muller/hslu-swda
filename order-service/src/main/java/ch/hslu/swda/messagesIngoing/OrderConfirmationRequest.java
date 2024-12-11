package ch.hslu.swda.messagesIngoing;

import java.util.UUID;

public record OrderConfirmationRequest(UUID orderId) implements IngoingMessage {
    @Override
    public UUID getOrderId() {
        return orderId;
    }
}
