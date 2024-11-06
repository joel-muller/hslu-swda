package ch.hslu.swda.business;

import ch.hslu.swda.entities.LogEntry;
import ch.hslu.swda.entities.LogFilter;

import ch.hslu.swda.entities.SortDirection;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Query;
import dev.morphia.query.Sort;
import org.bson.UuidRepresentation;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static dev.morphia.query.experimental.filters.Filters.eq;

public class LogsDatabase implements Logs {

    private final Datastore datastore;

    public LogsDatabase() {
        String mongoUri = System.getenv().getOrDefault("MONGO_URI", "mongodb://localhost:27017");
        ConnectionString connectionString = new ConnectionString(mongoUri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .build();
        datastore = Morphia.createDatastore(MongoClients.create(settings), "logs");
        datastore.getMapper().map(LogEntry.class);
        datastore.ensureIndexes();
    }

    public LogsDatabase(MongoClientSettings settings) {
        datastore = Morphia.createDatastore(MongoClients.create(settings), "logs");
        datastore.getMapper().map(LogEntry.class);
        datastore.ensureIndexes();
    }

    @Override
    public boolean addLogEntry(LogEntry logEntry) {
        datastore.save(logEntry);
        return false;
    }

    @Override
    public LogEntry getById(UUID id) {
        LogEntry logEntry = datastore.find(LogEntry.class)
                .filter(eq("_id", id))
                .first();
        return logEntry;
    }

    @Override
    public List<LogEntry> findByFilter(LogFilter filter) {
        Query<LogEntry> query = datastore.find(LogEntry.class);
        if (filter.getAmount() < 1) {
            return Collections.emptyList();
        }

        if (!filter.getObjUuid().isEmpty()) {
            query.filter(eq("objUuid", UUID.fromString(filter.getObjUuid())));
        }
        if (!filter.getUserId().isEmpty()) {
            query.filter(eq("userId", UUID.fromString(filter.getUserId())));
        }
        if (!filter.getEventType().isEmpty()) {
            query.filter(eq("eventType", filter.getEventType()));
        }
        if (!filter.getSource().isEmpty()) {
            query.filter(eq("source", filter.getSource()));
        }

        if (filter.getDirection().equals(SortDirection.ASC)) {
            return query.iterator(new FindOptions()
                    .sort(Sort.ascending("timestamp"))
                    .limit(filter.getAmount()))
                    .toList();
        } else {
            return query.iterator(new FindOptions()
                    .sort(Sort.descending("timestamp"))
                    .limit(filter.getAmount())).toList();
        }
    }

    @Override
    public List<LogEntry> findByEventType(String event, int amount) {
        List<LogEntry> logList = datastore.find(LogEntry.class)
                .filter(eq("eventType", event))
                .iterator(new FindOptions()
                        .sort(Sort.descending("timestamp"))
                        .limit(amount))
                .toList();
        return logList;
    }

    @Override
    public List<LogEntry> findByUserId(UUID userId, int amount) {
        List<LogEntry> logList = datastore.find(LogEntry.class)
                .filter(eq("userId", userId))
                .iterator(new FindOptions()
                        .sort(Sort.descending("timestamp"))
                        .limit(amount))
                .toList();
        return logList;
    }

    @Override
    public List<LogEntry> findBySource(String source, int amount) {
        List<LogEntry> logList = datastore.find(LogEntry.class)
                .filter(eq("source", "order"))
                .iterator(new FindOptions()
                        .sort(Sort.descending("timestamp"))
                        .limit(amount))
                .toList();
        return logList;
    }

    @Override
    public List<LogEntry> findByObjectUUID(UUID uuid) {
        List<LogEntry> logList = datastore.find(LogEntry.class)
                .filter(eq("objUuid", uuid))
                .iterator(new FindOptions()
                        .sort(Sort.descending("timestamp")))
                .toList();
        return logList;
    }

    @Override
    public List<LogEntry> getRecent(int amount) {
        List<LogEntry> logList = datastore.find(LogEntry.class)
                .iterator(new FindOptions()
                        .sort(Sort.descending("timestamp"))
                        .limit(amount))
                .toList();
        return logList;
    }
}
