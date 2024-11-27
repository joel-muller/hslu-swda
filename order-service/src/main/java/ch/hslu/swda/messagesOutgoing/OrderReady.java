package ch.hslu.swda.messagesOutgoing;

import java.util.UUID;

public record OrderReady(UUID orderId, UUID storeId) implements OutgoingMessage {
}
