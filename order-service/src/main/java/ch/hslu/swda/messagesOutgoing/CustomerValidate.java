package ch.hslu.swda.messagesOutgoing;

import java.util.UUID;

public record CustomerValidate(UUID customerId, UUID employeeId, UUID orderId) implements OutgoingMessage { }
