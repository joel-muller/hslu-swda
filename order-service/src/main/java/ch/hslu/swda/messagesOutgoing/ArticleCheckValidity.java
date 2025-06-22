package ch.hslu.swda.messagesOutgoing;

import java.util.*;

public record ArticleCheckValidity(UUID orderId, Map<Integer, Integer> articles, UUID employeeId) implements OutgoingMessage {

}
