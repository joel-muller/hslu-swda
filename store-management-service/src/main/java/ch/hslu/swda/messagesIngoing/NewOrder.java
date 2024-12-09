package ch.hslu.swda.messagesIngoing;

import java.util.Map;
import java.util.UUID;

public record NewOrder(UUID orderId, UUID employeeId, UUID storeId, Map<Integer, Integer> articles) implements IngoingMessage {
    @Override
    public UUID getStoreId() {
        return storeId;
    }
}
