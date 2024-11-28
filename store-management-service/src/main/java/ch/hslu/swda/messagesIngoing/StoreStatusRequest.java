package ch.hslu.swda.messagesIngoing;

import java.util.Map;
import java.util.UUID;

public record StoreStatusRequest(UUID storeId, Map<String,Integer> params) {
}
