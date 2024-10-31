package ch.hslu.swda.business;

import ch.hslu.swda.entities.LogEntry;
import ch.hslu.swda.entities.LogFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public final class LogsMemory implements Logs {

    private static final Logger LOG = LoggerFactory.getLogger(LogsMemory.class);
    private static Map<UUID, LogEntry> logEntryMap = new HashMap<>();

    /**
     * Constructor for testing
     * @param map
     */
    LogsMemory(Map map) {
        logEntryMap = new HashMap<>();
    }

    /**
     * Default constructor
     */
    public LogsMemory() {
        // empty
    }
    @Override
    public boolean addLogEntry(LogEntry logEntry) {
        logEntryMap.put(logEntry.getId(), logEntry);
        LOG.info("Log stored: " + logEntry);
        return true;
    }

    @Override
    public LogEntry getById(UUID id) {
        LOG.info("Log " + id + " retrieved");
        return logEntryMap.get(id);
    }

    @Override
    public List<LogEntry> findByFilter(LogFilter filter) {
        List<LogEntry> logList = logEntryMap.entrySet().stream()
                .filter(e -> filter.getSource().isEmpty() || filter.getSource().equals(e.getValue().getSource()))
                .filter(e -> filter.getUserId() == -1 || filter.getUserId() == e.getValue().getUserId())
                .filter(e -> filter.getEventType().isEmpty() || filter.getEventType().equals(e.getValue().getEventType()))
                .filter(e -> filter.getObjUuid().isEmpty() || filter.getObjUuid().equals(e.getValue().getObjUuid()))
                .map(Map.Entry::getValue)
                .sorted(Comparator.comparing(LogEntry::getTimestamp).reversed())
                .limit(filter.getAmount())
                .collect(Collectors.toList());
        LOG.info("Filtered log list retrieved");
        return logList;
    }

    @Override
    public List<LogEntry> findByEventType(String event, int amount) {
        List<LogEntry> logList = logEntryMap.entrySet().stream()
                .filter(e -> event.equals(e.getValue().getEventType()))
                .map(Map.Entry::getValue)
                .sorted(Comparator.comparing(LogEntry::getTimestamp).reversed())
                .limit(amount)
                .collect(Collectors.toList());
        LOG.info("Event log list for " + event + " of size " + logList.size() + " retrieved");
        return logList;
    }

    @Override
    public List<LogEntry> findByUserId(int userId, int amount) {
        List<LogEntry> logList = logEntryMap.entrySet().stream()
                .filter(e -> userId == e.getValue().getUserId())
                .map(Map.Entry::getValue)
                .sorted(Comparator.comparing(LogEntry::getTimestamp).reversed())
                .limit(amount)
                .collect(Collectors.toList());
        LOG.info("UserId log list for " + userId + " of size " + logList.size() + " retrieved");
        return logList;
    }

    @Override
    public List<LogEntry> findBySource(String source, int amount) {
        List<LogEntry> logList = logEntryMap.entrySet().stream()
                .filter(e -> source.equals(e.getValue().getSource()))
                .map(Map.Entry::getValue)
                .sorted(Comparator.comparing(LogEntry::getTimestamp).reversed())
                .limit(amount)
                .collect(Collectors.toList());
        LOG.info("Source log list for " + source + " of size " + logList.size() + " retrieved");
        return logList;
    }

    @Override
    public List<LogEntry> findByObjectUUID(UUID uuid) {
        List<LogEntry> logList = logEntryMap.entrySet().stream()
                .filter(e -> uuid.equals(e.getValue().getObjUuid()))
                .map(Map.Entry::getValue)
                .sorted(Comparator.comparing(LogEntry::getTimestamp).reversed())
                .collect(Collectors.toList());
        LOG.info("Object log list for " + uuid + " of size " + logList.size() + " retrieved");
        return logList;
    }

    @Override
    public List<LogEntry> getRecent(int amount) {
        List<LogEntry> logList = logEntryMap.entrySet().stream()
                .map(Map.Entry::getValue)
                .sorted(Comparator.comparing(LogEntry::getTimestamp).reversed())
                .limit(amount)
                .collect(Collectors.toList());
        LOG.info("Log list of size " + logList.size() + " retrieved");
        return logList;
    }
}
