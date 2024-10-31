package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.RabbitMqConfig;
import ch.hslu.swda.model.LogEntry;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.core.type.TypeReference;
import groovy.util.logging.Log;
import io.micronaut.core.type.Argument;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.serde.ObjectMapper;
import io.micronaut.serde.jackson.JacksonJsonMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Controller for retrieving logs.
 */
@Tag(name = "Log")

@Controller("/api/v1/logs")
public class LogController {
    private static final Logger LOG = LoggerFactory.getLogger(LogController.class);
    private String exchangeName = new RabbitMqConfig().getExchange();
    private BusConnector bus = new BusConnector();
    @Inject
    private ObjectMapper mapper;

    @Get("/")
    public List<LogEntry> getRecent() {
        try {
            bus.connect();
            String reply = bus.talkSync(exchangeName, "logs.get", "");
            List<LogEntry> list = mapper.readValue(reply, Argument.listOf(LogEntry.class));
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
