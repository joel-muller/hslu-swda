package ch.hslu.swda.messages;

import ch.hslu.swda.entities.State;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LogMessageTest {

    @Test
    void getSource() {
        UUID id = UUID.randomUUID();
        UUID empId = UUID.randomUUID();
        String eventType = "order service";
        String msg = "Hello world";
        LogMessage message = new LogMessage(id, empId, eventType, msg);
        assertEquals("order.service", message.getSource());
    }

    @Test
    void getUserId() {
        UUID id = UUID.randomUUID();
        UUID empId = UUID.randomUUID();
        String eventType = "order service";
        String msg = "Hello world";
        LogMessage message = new LogMessage(id, empId, eventType, msg);
        assertEquals(empId, message.getUserId());
    }

    @Test
    void getEventType() {
        UUID id = UUID.randomUUID();
        UUID empId = UUID.randomUUID();
        String eventType = "ordercreated";
        String msg = "Hello world";
        LogMessage message = new LogMessage(id, empId, eventType, msg);
        assertEquals(eventType, message.getEventType());
    }

    @Test
    void getObjUuid() {
        UUID id = UUID.randomUUID();
        UUID empId = UUID.randomUUID();
        String eventType = "order service";
        String msg = "Hello world";
        LogMessage message = new LogMessage(id, empId, eventType, msg);
        assertEquals(id, message.getObjUuid());
    }

    @Test
    void getMessage() {
        UUID id = UUID.randomUUID();
        UUID empId = UUID.randomUUID();
        String eventType = "order service";
        String msg = "Hello world";
        LogMessage message = new LogMessage(id, empId, eventType, msg);
        assertEquals(msg, message.getMessage());
    }

    @Test
    void testEquals() {
        UUID id = UUID.randomUUID();
        UUID empId = UUID.randomUUID();
        String eventType = "order service";
        String msg = "Hello world";
        LogMessage message = new LogMessage(id, empId, eventType, msg);
        LogMessage message2 = new LogMessage(id, empId, eventType, msg);
        assertEquals(message, message2);
    }

    @Test
    void testHashCode() {
        UUID id = UUID.randomUUID();
        UUID empId = UUID.randomUUID();
        String eventType = "order service";
        String msg = "Hello world";
        LogMessage message = new LogMessage(id, empId, eventType, msg);
        LogMessage message2 = new LogMessage(id, empId, eventType, msg);
        assertEquals(message.hashCode(), message2.hashCode());
    }

    @Test
    void testEqualsEqualsVerifier() {
        EqualsVerifier.simple().forClass(LogMessage.class)
                .withIgnoredFields("timestamp")
                .verify();

    }

    @Test
    void testToString() {
        UUID id = UUID.randomUUID();
        UUID empId = UUID.randomUUID();
        String eventType = "order service";
        String msg = "Hello world";
        LogMessage message = new LogMessage(id, empId, eventType, msg);
        assertEquals("LogMessage{message='Hello world'}", message.toString());
    }

}