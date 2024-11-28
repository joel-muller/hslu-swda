package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.RabbitMqConfig;
import ch.hslu.swda.model.OrderStatus;
import ch.hslu.swda.model.log.LogEntry;
import ch.hslu.swda.model.log.LogFilter;
import ch.hslu.swda.model.log.SortDirection;
import io.micronaut.core.type.Argument;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.serde.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * Controller for retrieving logs.
 */
@Tag(name = "Log")

@Controller("/api/v1/stores/{storeId}")
public class StoreStatusController {
    private static final Logger LOG = LoggerFactory.getLogger(StoreStatusController.class);
    private String exchangeName = new RabbitMqConfig().getExchange();
    private BusConnector bus = new BusConnector();
    @Inject
    private ObjectMapper mapper;

    @Get("/orders")
    public List<OrderStatus> orders(@PathVariable UUID storeId) {
        return new ArrayList<OrderStatus>();
    }

}
