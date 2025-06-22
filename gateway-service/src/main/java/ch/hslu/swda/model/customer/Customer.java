package ch.hslu.swda.model.customer;

import java.util.UUID;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
public record Customer(UUID id, String firstname, String lastname) {
}
