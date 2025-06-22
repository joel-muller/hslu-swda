package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.RabbitMqConfig;
import ch.hslu.swda.model.Order;
import ch.hslu.swda.model.orderConfirmation.OrderConfirmationDTOWrapper;
import ch.hslu.swda.model.orderConfirmation.OrderConfirmationRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.serde.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * Controller for retrieving logs.
 */
@Tag(name = "Order")

@Controller("/api/v1/orders")
public class OrderController {
    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);
    private String exchangeName = new RabbitMqConfig().getExchange();
    private BusConnector bus = new BusConnector();
    @Inject
    private ObjectMapper mapper;

    @Post("/")
    public String receive(@Body Order order) {
        try {
            bus.connect();
            String reply = bus.talkSync(exchangeName, "order.receive", mapper.writeValueAsString(order));
            if (reply == null) {
                LOG.info("received no empty reply from service");
            } else {
                LOG.info("received reply: " + reply);
            }
            return reply;

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Get("/{orderId}/confirmation")
    @Operation(
            summary = "Get order confirmation",
            description = "Fetches the order confirmation for the selected order."
    )
    public HttpResponse<?> getOrderConfirmation(
            @Parameter(
                    description = "The orderId of the order from which to get the order confirmation",
                    example = "baecffa7-f8cb-4629-9951-b1aa181c9f70",
                    required = true
            )
            @PathVariable final String orderId

    ) {

        LOG.info("Received orderConfirmationRequest with orderId {}",orderId);


        OrderConfirmationDTOWrapper wrapper;
        try {
            bus.connect();
            String reply = bus.talkSync(exchangeName, "order.confirmation.get", mapper.writeValueAsString(new OrderConfirmationRequest(UUID.fromString(orderId))));
            LOG.info("Got reply from bus: [{}]. Sending reply to client", reply);
            wrapper = mapper.readValue(reply, OrderConfirmationDTOWrapper.class);
            if (wrapper.valid()) {
                return HttpResponse.ok(wrapper.orderConfirmationDTO());
            } else {
                return HttpResponse.ok("{}");
            }
        } catch (IOException | TimeoutException | InterruptedException e) {
            LOG.error("Error while fetching confirmation: {}", e.getMessage());
            return HttpResponse.serverError("An error occurred while processing the request. Please try again later.");
        }

    }
}
