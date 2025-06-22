package ch.hslu.swda.model.user;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
public record UserJWT(String jwt, boolean success) {
}
