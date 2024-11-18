package ch.hslu.swda.messages;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public record OrderRequest(UUID orderId, UUID employeeId, UUID storeId, Map<Integer, Integer> articles) implements IngoingMessage {
}
