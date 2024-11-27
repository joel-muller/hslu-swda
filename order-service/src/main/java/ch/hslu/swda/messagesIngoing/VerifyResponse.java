package ch.hslu.swda.messagesIngoing;

import java.util.UUID;

public record VerifyResponse(UUID idOrder, boolean valid) implements IngoingMessage {

    @Override
    public UUID getOrderId() {
        return idOrder;
    }
}
