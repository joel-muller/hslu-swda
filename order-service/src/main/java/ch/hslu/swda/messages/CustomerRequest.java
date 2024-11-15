package ch.hslu.swda.messages;

import java.util.UUID;

public record CustomerRequest(UUID customerId, UUID employeeId, UUID orderId) implements OutgoingMessage { }
