package ch.hslu.swda.business;

import ch.hslu.swda.entities.LogEntry;
import ch.hslu.swda.entities.LogFilter;

import java.util.List;
import java.util.UUID;

/**
 * Interface for storing logs.
 */
public interface Logs {

    /**
     * Stores a log entry.
     * @param logEntry
     * @return true on success, false on failure
     */
    boolean addLogEntry(LogEntry logEntry);

    /**
     * Returns a log.
     * @param id
     * @return LogEntry
     */
    LogEntry getById(UUID id);

    /**
     * Returns a specified amount of logs based on optional filters in a {@code LogFilter} object.
     * @param filter
     * @return
     */
    List<LogEntry> findByFilter(LogFilter filter);

    /**
     * Returns a maximum amount of logs related to an event type. Wildcards are allowed.
     * @param event
     * @return List of log entries
     */
    List<LogEntry> findByEventType(String event, int amount);

    /**
     * Returns a maximum amount of logs related to a user.
     * @param userId
     * @param amount
     * @return List of log entries
     */
    List<LogEntry> findByUserId(int userId, int amount);

    /**
     * Returns a maximum amount of logs related to a service.
     * @param source
     * @param amount
     * @return List of log entries
     */
    List<LogEntry> findBySource(String source, int amount);

    /**
     * Returns all logs related to an object across all services.
     * @param uuid
     * @return List of log entries
     */
    List<LogEntry> findByObjectUUID(UUID uuid);

    /**
     * Returns a maximum amount of recent logs.
     * @param amount
     * @return List of log entries
     */
    List<LogEntry> getRecent(int amount);
}
