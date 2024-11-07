package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.business.Logs;
import ch.hslu.swda.business.LogsDatabase;
import ch.hslu.swda.entities.LogEntry;
import ch.hslu.swda.entities.LogFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class LogFilterRetriever implements MessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(LogFilterRetriever.class);
    private final String exchangeName;
    private final BusConnector bus;

    private final Logs logs = new LogsDatabase();

    public LogFilterRetriever(final String exchangeName, final BusConnector bus) {
        this.exchangeName = exchangeName;
        this.bus = bus;
    }
    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        try {
            LOG.debug("Received get filter request with replyTo [{}]", replyTo);
            ObjectMapper mapper = new ObjectMapper();
            LogFilter filter = mapper.readValue(message, LogFilter.class);
            List<LogEntry> list = logs.findByFilter(filter);
            LOG.debug("Sending list of log entries with size [{}]", list.size());
            bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(list));
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
