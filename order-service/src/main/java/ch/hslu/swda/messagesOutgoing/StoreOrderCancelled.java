package ch.hslu.swda.messagesOutgoing;

import java.util.UUID;

public record StoreOrderCancelled(UUID orderId, UUID storeId) implements OutgoingMessage {
}
