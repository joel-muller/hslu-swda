package ch.hslu.swda.messagesIngoing;

import java.util.Map;
import java.util.UUID;

public record InventoryUpdate(Map<Integer, Integer> articles, UUID orderId, UUID storeId) implements IngoingMessage {
    @Override
    public UUID getStoreId() {
        return storeId;
    }
}
