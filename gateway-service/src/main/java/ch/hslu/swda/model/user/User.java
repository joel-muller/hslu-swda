package ch.hslu.swda.model.user;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

import java.util.UUID;

@Introspected
@Serdeable
public record User(UUID id, String username, String roleName) {
}
