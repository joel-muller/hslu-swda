package ch.hslu.swda.model;

import java.util.UUID;
import io.micronaut.core.annotation.Introspected;

@Introspected
public record LogEntry(UUID id, String source, long timestamp, int userId, String eventType, UUID objUuid, String message) {

}
