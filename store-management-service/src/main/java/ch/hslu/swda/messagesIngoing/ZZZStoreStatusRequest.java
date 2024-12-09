package ch.hslu.swda.messagesIngoing;

import java.util.Map;
import java.util.UUID;

public record ZZZStoreStatusRequest(UUID storeId, Map<String,Integer> params) {
}
