package ch.hslu.swda.messagesOutgoing;

import java.util.UUID;

public record StoreOrderReady(UUID orderId, UUID storeId) implements OutgoingMessage {
}
