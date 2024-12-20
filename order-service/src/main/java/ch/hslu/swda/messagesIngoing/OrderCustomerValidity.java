package ch.hslu.swda.messagesIngoing;

import java.util.UUID;

public record OrderCustomerValidity(UUID orderId, boolean exists) implements IngoingMessage {
    @Override
    public UUID getOrderId() {
        return orderId;
    }
}
