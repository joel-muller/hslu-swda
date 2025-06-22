package ch.hslu.swda.model.orderConfirmation;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
public record OrderConfirmationDTOWrapper(boolean valid, OrderConfirmationDTO orderConfirmationDTO) {
}
