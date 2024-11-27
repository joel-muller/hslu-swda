package ch.hslu.swda.messages;

import java.util.UUID;

public record OrderReady(UUID orderId, UUID storeId) implements OutgoingMessage {
}