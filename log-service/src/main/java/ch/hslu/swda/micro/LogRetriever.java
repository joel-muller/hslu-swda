package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.business.Logs;
import ch.hslu.swda.business.LogsMemory;
import ch.hslu.swda.entities.LogEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class LogRetriever implements MessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(LogRetriever.class);
    private final String exchangeName;
    private final BusConnector bus;

    private final Logs logs = new LogsMemory();

    public LogRetriever(final String exchangeName, final BusConnector bus) {
        this.exchangeName = exchangeName;
        this.bus = bus;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        try {
            LOG.debug("Received get request with replyTo [{}]", replyTo);
            ObjectMapper mapper = new ObjectMapper();
            List<LogEntry> list = logs.getRecent(100);
            LOG.debug("Sending list of log entries with size [{}]", list.size());
            bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(list));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
