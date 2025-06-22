package ch.hslu.swda.messagesIngoing;

import java.util.Map;
import java.util.UUID;

public record OrderReceiveValidity(UUID idOrder, boolean valid, Map<Integer, Integer> francsPerUnit, Map<Integer, Integer> centimesPerUnit) implements IngoingMessage {

    @Override
    public UUID getOrderId() {
        return idOrder;
    }
}
