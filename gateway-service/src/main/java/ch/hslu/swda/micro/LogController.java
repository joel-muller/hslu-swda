package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.RabbitMqConfig;
import ch.hslu.swda.business.TokenAuthenticator;
import ch.hslu.swda.model.auth.AuthenticatedRequestNoBody;
import ch.hslu.swda.model.log.LogEntry;
import ch.hslu.swda.model.log.LogFilter;
import ch.hslu.swda.model.log.SortDirection;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.serde.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
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

    @Get("/{?source,userId,eventType,objUuid,sort,amount}")
    public List<LogEntry> getRecent(
            @QueryValue("source") @Nullable final String source,
            @QueryValue("userId") @Nullable final String userId,
            @QueryValue("eventType") @Nullable final String eventType,
            @QueryValue("objUuid") @Nullable final String objUuid,
            @QueryValue("sort") @Nullable final String sort,
            @QueryValue("amount") @Nullable final Integer amount
    ) {
        boolean filterSpecified = source != null || userId != null || eventType != null || objUuid != null || sort != null || amount != null;
        if (filterSpecified) {
            String finalSource = (source != null) ? source : "";
            String finalUserId = (userId != null) ? userId : "";
            String finalEventType = (eventType != null) ? eventType : "";
            String finalObjUuid = (objUuid != null) ? objUuid : "";
            SortDirection finalSort = (sort != null && (sort.equals("asc") || sort.equals("desc"))) ? SortDirection.valueOf(sort.toUpperCase()) : SortDirection.DESC;
            int finalAmount = (amount != null) ? amount : 100;
            LogFilter filter = new LogFilter(finalSource, finalUserId, finalEventType, finalObjUuid, finalSort, finalAmount);

            try {
                bus.connect();
                String reply = bus.talkSync(exchangeName, "logs.filter", mapper.writeValueAsString(filter));
                List<LogEntry> list = mapper.readValue(reply, Argument.listOf(LogEntry.class));
                return list;
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (TimeoutException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
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

    @Get("/{id}")
    public LogEntry getLog(@PathVariable UUID id) {
        try {
            bus.connect();
            String reply = bus.talkSync(exchangeName, "logs.get", mapper.writeValueAsString(id));
            LogEntry logEntry = mapper.readValue(reply, LogEntry.class);
            return logEntry;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
