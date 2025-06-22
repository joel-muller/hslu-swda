package ch.hslu.swda.model.orderConfirmation;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

import java.util.UUID;
@Introspected
@Serdeable
public record OrderConfirmationRequest(UUID orderId)   {
    public UUID getOrderId() {
        return orderId;
    }
}
