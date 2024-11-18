package ch.hslu.swda.messages;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;


public class LogMessage implements OutgoingMessage {
    private final String source;
    private final long timestamp;
    private final UUID userId;
    private final String eventType;
    private final UUID objUuid;
    private final String message;

    public LogMessage(UUID orderId, UUID userId, String eventType, String message) {
        this.source = "store-management.service";
        this.timestamp = Instant.now().getEpochSecond();
        this.userId = userId;
        this.eventType = eventType;
        this.objUuid = orderId;
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
        if (!(o instanceof LogMessage that)) return false;
        return Objects.equals(source, that.source) && Objects.equals(userId, that.userId) && Objects.equals(eventType, that.eventType) && Objects.equals(objUuid, that.objUuid) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, userId, eventType, objUuid, message);
    }

    @Override
    public String toString() {
        return "LogMessage{" +
                "message='" + message + '\'' +
                '}';
    }


}