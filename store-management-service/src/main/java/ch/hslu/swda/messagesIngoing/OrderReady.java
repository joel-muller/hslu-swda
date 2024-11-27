package ch.hslu.swda.messagesIngoing;

import java.util.UUID;

public record OrderReady(UUID orderId, UUID storeId) implements IngoingMessage {
    @Override
    public UUID getStoreId() {
        return storeId;
    }
}