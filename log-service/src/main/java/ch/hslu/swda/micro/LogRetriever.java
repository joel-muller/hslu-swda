package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.business.Logs;
import ch.hslu.swda.business.LogsDatabase;
import ch.hslu.swda.business.LogsMemory;
import ch.hslu.swda.entities.LogEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class LogRetriever implements MessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(LogRetriever.class);
    private final String exchangeName;
    private final BusConnector bus;

    private final Logs logs = new LogsDatabase();

    public LogRetriever(final String exchangeName, final BusConnector bus) {
        this.exchangeName = exchangeName;
        this.bus = bus;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        try {
            LOG.debug("Received get request with replyTo [{}]", replyTo);
            ObjectMapper mapper = new ObjectMapper();
            if (message.isEmpty()) {
                List<LogEntry> list = logs.getRecent(100);
                LOG.debug("Sending list of log entries with size [{}]", list.size());
                bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(list));
            } else {
                UUID logId = mapper.readValue(message, UUID.class);
                LogEntry logEntry = logs.getById(logId);
                if (logEntry != null) {
                    LOG.debug("Sending log entry with id [{}]", logEntry.getId());
                } else {
                    LOG.debug("Log with id [{}] attempted to retrieve, does not exist", logId);
                }
                bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(logEntry));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
