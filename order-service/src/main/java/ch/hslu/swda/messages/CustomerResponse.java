package ch.hslu.swda.messages;

import java.util.UUID;

public record CustomerResponse(UUID orderId, boolean exists) implements IngoingMessage {
    @Override
    public UUID getOrderId() {
        return orderId;
    }
}
