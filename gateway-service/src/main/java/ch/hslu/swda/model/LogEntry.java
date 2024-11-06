package ch.hslu.swda.model;

import java.util.UUID;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
public record LogEntry(UUID id, String source, long timestamp, UUID userId, String eventType, UUID objUuid, String message) {

}
