package ch.hslu.swda.messagesOutgoing;

import java.util.Map;
import java.util.UUID;

public record WarehouseRequest(UUID orderId, UUID storeId, Map<Integer, Integer> articles) implements OutgoingMessage {
}
