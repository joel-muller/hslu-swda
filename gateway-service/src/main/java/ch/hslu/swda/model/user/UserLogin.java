package ch.hslu.swda.model.user;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
public record UserLogin(String username, String password) {
}
