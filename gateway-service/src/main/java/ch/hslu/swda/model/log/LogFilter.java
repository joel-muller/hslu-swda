package ch.hslu.swda.model.log;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
public record LogFilter(String source, String userId, String eventType, String objUuid, SortDirection direction, int amount) {
}
