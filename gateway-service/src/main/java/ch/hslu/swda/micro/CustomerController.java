package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.RabbitMqConfig;
import ch.hslu.swda.model.customer.Customer;
import ch.hslu.swda.model.log.LogEntry;
import io.micronaut.core.type.Argument;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.serde.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Controller to do CRUD operations on customers.
 */
@Tag(name = "Customer")

@Controller("/api/v1/customers")
public class CustomerController {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerController.class);
    private String exchangeName = new RabbitMqConfig().getExchange();
    private BusConnector bus = new BusConnector();
    @Inject
    private ObjectMapper mapper;

    @Get("/")
    public List<Customer> getCustomers() {
        try {
            bus.connect();
            String reply = bus.talkSync(exchangeName, "customer.get", "");
            List<Customer> list = mapper.readValue(reply, Argument.listOf(Customer.class));
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Post("/")
    public Customer createCustomer(@Body Customer customer) {
        try {
            bus.connect();
            String reply = bus.talkSync(exchangeName, "customer.create", mapper.writeValueAsString(customer));
            LOG.info(reply);
            Customer receivedCustomer = mapper.readValue(reply, Customer.class);
            return receivedCustomer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
