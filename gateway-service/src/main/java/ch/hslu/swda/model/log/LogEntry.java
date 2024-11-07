package ch.hslu.swda.model.log;

import java.util.UUID;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
public record LogEntry(UUID id, String source, long timestamp, int userId, String eventType, UUID objUuid, String message) {

}
