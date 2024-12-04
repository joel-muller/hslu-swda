package ch.hslu.swda.messagesIngoing;

import java.util.List;
import java.util.UUID;

public record OrderUpdate(UUID id, List<Integer> articles, boolean storeValid) implements IngoingMessage {
    @Override
    public UUID getOrderId() {
        return id;
    }
}
