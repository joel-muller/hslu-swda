package ch.hslu.swda.entities;

import java.util.Objects;
import java.util.UUID;
import java.time.Instant;


public class LogMessage {
    private final String source;
    private final long timestamp;
    private final UUID userId;
    private final String eventType;
    private final UUID objUuid;
    private final String message;

    public LogMessage(UUID userId, String eventType, String message) {
        this.source = "order.service";
        this.timestamp = Instant.now().getEpochSecond();
        this.userId = userId;
        this.eventType = eventType;
        this.objUuid = UUID.randomUUID();
        this.message = message;
    }

    public String getSource() {
        return source;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getEventType() {
        return eventType;
    }

    public UUID getObjUuid() {
        return objUuid;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogMessage that = (LogMessage) o;
        return Objects.equals(objUuid, that.objUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(objUuid);
    }

    @Override
    public String toString() {
        return "LogMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}