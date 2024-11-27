package ch.hslu.swda.messages;

import java.util.Map;
import java.util.UUID;

public record InventoryUpdate(UUID orderId, UUID storeId, Map<Integer, Integer> articles) implements IngoingMessage {
}
