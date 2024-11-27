package ch.hslu.swda.messagesOutgoing;

import java.util.UUID;

public record CustomerRequest(UUID customerId, UUID employeeId, UUID orderId) implements OutgoingMessage { }
