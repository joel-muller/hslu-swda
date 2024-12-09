package ch.hslu.swda.messagesIngoing;

import java.util.UUID;

public record CancelOrder(UUID orderId, UUID storeId) implements IngoingMessage {
    @Override
    public UUID getStoreId() {
        return orderId;
    }
}
