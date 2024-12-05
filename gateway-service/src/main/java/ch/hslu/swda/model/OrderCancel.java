package ch.hslu.swda.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

import java.util.UUID;

@Introspected @Serdeable
public record OrderCancel(UUID orderId) {
}
