package ch.hslu.swda.entities;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class LogEntryTest {

    @Test
    public void testLogEntryCreation() {
        String source = "testSource";
        long timestamp = System.currentTimeMillis();
        UUID userId = UUID.randomUUID();
        String eventType = "testEvent";
        UUID objUuid = UUID.randomUUID();
        String message = "testMessage";

        LogEntry logEntry = new LogEntry(source, timestamp, userId, eventType, objUuid, message);

        assertThat(logEntry.source()).isEqualTo(source);
        assertThat(logEntry.timestamp()).isEqualTo(timestamp);
        assertThat(logEntry.userId()).isEqualTo(userId);
        assertThat(logEntry.eventType()).isEqualTo(eventType);
        assertThat(logEntry.objUuid()).isEqualTo(objUuid);
        assertThat(logEntry.message()).isEqualTo(message);
    }
}