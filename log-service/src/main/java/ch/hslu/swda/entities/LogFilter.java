package ch.hslu.swda.entities;

import java.util.UUID;

/**
 * Holds various filter options to be used when filtering or sorting logs.
 */
public class LogFilter {
    private String source;
    private String userId;
    private String eventType;
    private String objUuid;
    private SortDirection direction;
    private int amount;

    /**
     * Default constructor
     */
    public LogFilter() {
        this.source = "";
        this.userId = "";
        this.eventType = "";
        this.objUuid = "";
        this.direction = SortDirection.DESC;
        this.amount = 100;
    }

    /**
     * Constructor to create a filter for log entries
     * @param source
     * @param userId
     * @param eventType
     * @param objUuid
     * @param direction
     * @param amount
     */
    public LogFilter(String source, String userId, String eventType, String objUuid, SortDirection direction, int amount) {
        this.source = source;
        this.userId = userId;
        this.eventType = eventType;
        this.objUuid = objUuid;
        this.direction = direction;
        this.amount = amount;
    }

    public String getSource() {
        return source;
    }

    public String getUserId() {
        return userId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getObjUuid() {
        return objUuid;
    }

    public SortDirection getDirection() {
        return direction;
    }

    public int getAmount() {
        return amount;
    }
}
