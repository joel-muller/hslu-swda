package ch.hslu.swda.business;

import ch.hslu.swda.entities.LogEntry;
import ch.hslu.swda.entities.LogFilter;
import ch.hslu.swda.entities.SortDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LogsMemoryTest {

    private LogsMemory logsMemory;
    private List<UUID> objUUIDList;
    private List<UUID> userUUIDList;
    private List<LogEntry> logList;

    @BeforeEach
    void prepare() {
        logsMemory = new LogsMemory(new HashMap<>());
        objUUIDList = new ArrayList<>();
        userUUIDList = new ArrayList<>();
        logList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            objUUIDList.add(UUID.randomUUID());
            userUUIDList.add(UUID.randomUUID());
        }
        LogEntry logEntry1 = new LogEntry("order", Instant.now().getEpochSecond(), userUUIDList.get(0), "order.new",
                objUUIDList.get(0), "New order was created");
        logList.add(logEntry1);
        logsMemory.addLogEntry(logEntry1);
        LogEntry logEntry2 = new LogEntry("order", Instant.now().getEpochSecond(), userUUIDList.get(1), "order.update",
                objUUIDList.get(0), "Order was updated");
        logList.add(logEntry2);
        logsMemory.addLogEntry(logEntry2);
        LogEntry logEntry3 = new LogEntry("store", Instant.now().getEpochSecond(), userUUIDList.get(0), "restock.receive",
                objUUIDList.get(1), "Restock order was received");
        logList.add(logEntry3);
        logsMemory.addLogEntry(logEntry3);
        LogEntry logEntry4 = new LogEntry("accounting", Instant.now().getEpochSecond(), userUUIDList.get(3), "order.invoice",
                objUUIDList.get(0), "New invoice was created");
        logList.add(logEntry4);
        logsMemory.addLogEntry(logEntry4);
        LogEntry logEntry5 = new LogEntry("order", Instant.now().getEpochSecond(), userUUIDList.get(1), "order.new",
                objUUIDList.get(2), "New order was created");
        logList.add(logEntry5);
        logsMemory.addLogEntry(logEntry5);
    }

    @Test
    void testGetById() {
        LogEntry logEntry = logsMemory.getById(logList.get(0).getId());
        assertEquals(logEntry, logList.get(0));
    }

    @Test
    void testFindByEventType() {
        List<LogEntry> list = logsMemory.findByEventType("order.new", 100);
        assertEquals(2, list.size());
        assertTrue(list.contains(logList.get(0)));
        assertTrue(list.contains(logList.get(4)));
    }

    @Test
    void findByUserId() {
        List<LogEntry> list = logsMemory.findByUserId(userUUIDList.get(1), 100);
        assertEquals(2, list.size());
        assertTrue(list.contains(logList.get(1)));
        assertTrue(list.contains(logList.get(4)));
    }

    @Test
    void findBySource() {
        List<LogEntry> list = logsMemory.findBySource("order", 100);
        assertEquals(3, list.size());
        assertTrue(list.contains(logList.get(0)));
        assertTrue(list.contains(logList.get(1)));
        assertTrue(list.contains(logList.get(4)));
    }

    @Test
    void findByObjectUUID() {
        List<LogEntry> list = logsMemory.findByObjectUUID(objUUIDList.get(0));
        assertEquals(3, list.size());
        assertTrue(list.contains(logList.get(0)));
        assertTrue(list.contains(logList.get(1)));
        assertTrue(list.contains(logList.get(3)));
    }

    @Test
    void getRecent() {
        List<LogEntry> list = logsMemory.getRecent(100);
        assertEquals(5, list.size());
    }

    @Test
    void findByFilter() {
        LogFilter filter = new LogFilter("order", "", "order.new", objUUIDList.get(2).toString(), SortDirection.DESC, 100);
        List<LogEntry> list = logsMemory.findByFilter(filter);
        assertEquals(1, list.size());
        assertTrue(list.contains(logList.get(4)));
    }
}