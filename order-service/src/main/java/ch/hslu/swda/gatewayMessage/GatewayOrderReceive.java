package ch.hslu.swda.gatewayMessage;

import java.util.Map;
import java.util.UUID;

public record GatewayOrderReceive(Map<Integer, Integer> articles, UUID storeId, UUID customerId, UUID employeeId) { }