package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.business.Logs;
import ch.hslu.swda.business.LogsDatabase;
import ch.hslu.swda.business.LogsMemory;
import ch.hslu.swda.entities.LogEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LogReceiver implements MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(LogReceiver.class);
    private final String exchangeName;
    private final BusConnector bus;

    private final Logs logs = new LogsDatabase();

    public LogReceiver(final String exchangeName, final BusConnector bus) {
        this.exchangeName = exchangeName;
        this.bus = bus;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        LOG.debug("Received message with routing [{}]", route);

        ObjectMapper mapper = new ObjectMapper();

        TypeReference<LogEntry> typeRef = new TypeReference<>() {};

        try {
            LogEntry logEntry = mapper.readValue(message, typeRef);
            logs.addLogEntry(logEntry);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
