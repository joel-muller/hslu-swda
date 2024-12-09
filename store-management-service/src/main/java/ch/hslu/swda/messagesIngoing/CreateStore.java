package ch.hslu.swda.messagesIngoing;

import java.util.UUID;

public record CreateStore(boolean addDefaultArticle) implements IngoingMessage {
    @Override
    public UUID getStoreId() {
        return null;
    }
}
