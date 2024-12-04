package ch.hslu.swda.messagesOutgoing;

import java.util.*;

public record VerifyRequest(UUID orderId, Map<Integer, Integer> articles, UUID employeeId) implements OutgoingMessage {

}
