package ch.hslu.swda.entities;

import java.util.UUID;

public record LogEntry(String source, long timestamp, UUID userId, String eventType, UUID objUuid, String message) {

}