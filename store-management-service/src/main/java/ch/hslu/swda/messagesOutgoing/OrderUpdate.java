package ch.hslu.swda.messagesOutgoing;

import java.util.List;
import java.util.UUID;

public record OrderUpdate(UUID id, List<Integer> articles) implements OutgoingMessage {}
