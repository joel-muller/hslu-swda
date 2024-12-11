package ch.hslu.swda.model.auth;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
public record AuthenticatedRequestNoBody(String jwt) {
}
