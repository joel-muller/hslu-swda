package ch.hslu.swda.messages;

import java.util.List;
import java.util.UUID;

public record OrderUpdate(UUID id, List<Integer> articles, boolean valid) implements IngoingMessage {
    @Override
    public UUID getOrderId() {
        return id;
    }
}
