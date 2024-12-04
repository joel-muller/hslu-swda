package ch.hslu.swda.messagesIngoing;

import java.util.Map;
import java.util.UUID;

public record CreateOrder(Map<Integer, Integer> articles, UUID storeId, UUID customerId, UUID employeeId) { }