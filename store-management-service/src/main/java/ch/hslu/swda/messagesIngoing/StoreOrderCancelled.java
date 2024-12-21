package ch.hslu.swda.messagesIngoing;

import java.util.UUID;

public record StoreOrderCancelled(UUID orderId, UUID storeId) implements IngoingMessage {
    @Override
    public UUID getStoreId() {
        return storeId;
    }
}
