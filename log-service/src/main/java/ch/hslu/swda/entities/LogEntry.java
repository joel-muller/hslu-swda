package ch.hslu.swda.entities;

import java.util.Objects;
import java.util.UUID;

/**
 * Holds the data necessary for a traceable log.
 */
public class LogEntry {
    private UUID id;
    private String source;
    private long timestamp;
    private int userId;
    private String eventType;
    private UUID objUuid;
    private String message;

    public LogEntry(String source, long timestamp, int userId, String eventType, UUID objUuid, String message) {
        this.id = UUID.randomUUID();
        this.source = source;
        this.timestamp = timestamp;
        this.userId = userId;
        this.eventType = eventType;
        this.objUuid = objUuid;
        this.message = message;
    }

    public UUID getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getUserId() {
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
        LogEntry logEntry = (LogEntry) o;
        return id.equals(logEntry.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "id=" + id +
                ", source='" + source + '\'' +
                ", timestamp=" + timestamp +
                ", userId=" + userId +
                ", eventType='" + eventType + '\'' +
                ", objUuid=" + objUuid +
                ", message='" + message + '\'' +
                '}';
    }
}
