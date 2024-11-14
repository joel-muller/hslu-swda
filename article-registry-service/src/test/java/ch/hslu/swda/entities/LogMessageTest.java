package ch.hslu.swda.entities;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LogMessageTest {

    @Test
    void getSource() {
        UUID id = UUID.randomUUID();
        String eventType = "order service";
        String msg = "Hello world";
        LogMessage message = new LogMessage(id, eventType, msg);
        assertEquals("article-registry.service", message.getSource());
    }

    @Test
    void getUserId() {
        UUID id = UUID.randomUUID();
        String eventType = "order service";
        String msg = "Hello world";
        LogMessage message = new LogMessage(id, eventType, msg);
        assertEquals(id, message.getUserId());
    }

    @Test
    void getEventType() {
        UUID id = UUID.randomUUID();
        String eventType = "ordercreated";
        String msg = "Hello world";
        LogMessage message = new LogMessage(id, eventType, msg);
        assertEquals(eventType, message.getEventType());
    }

    @Test
    void getObjUuid() {
        UUID id = UUID.randomUUID();
        String eventType = "order service";
        String msg = "Hello world";
        LogMessage message = new LogMessage(id, eventType, msg);
        assertInstanceOf(UUID.class, message.getObjUuid());
    }

    @Test
    void getMessage() {
        UUID id = UUID.randomUUID();
        String eventType = "order service";
        String msg = "Hello world";
        LogMessage message = new LogMessage(id, eventType, msg);
        assertEquals(msg, message.getMessage());
    }

    @Test
    void testEquals() {
    }

    @Test
    void testHashCode() {
    }

    @Test
    void testToString() {
        UUID id = UUID.randomUUID();
        String eventType = "order service";
        String msg = "Hello world";
        LogMessage message = new LogMessage(id, eventType, msg);
        assertEquals("LogMessage{message='Hello world'}", message.toString());
    }

}