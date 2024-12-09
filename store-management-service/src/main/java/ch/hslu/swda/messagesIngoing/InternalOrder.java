package ch.hslu.swda.messagesIngoing;

import java.util.Map;
import java.util.UUID;

public record InternalOrder(UUID storeId, Map<Integer, Integer> articles) implements IngoingMessage {
    @Override
    public UUID getStoreId() {
        return storeId;
    }
}
