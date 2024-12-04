package ch.hslu.swda.messagesOutgoing;

import java.util.UUID;

public record OrderCancelled(UUID orderId, UUID storeId) implements OutgoingMessage {
}
