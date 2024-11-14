package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.RabbitMqConfig;
import ch.hslu.swda.model.customer.Customer;
import ch.hslu.swda.model.log.LogEntry;
import io.micronaut.core.type.Argument;
import io.micronaut.http.annotation.*;
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

    /**
     * Gets all customers.
     * TODO: Filtering by firstname and lastname. This was not yet implemented due to a lack of time.
     * @return
     */
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

    /**
     * Gets a customer by their UUID.
     * @param id
     * @return
     */
    @Get("/{id}")
    public Customer getCustomerById(@PathVariable UUID id) {
        try {
            bus.connect();
            String reply = bus.talkSync(exchangeName, "customer.get", mapper.writeValueAsString(id));
            Customer customer = mapper.readValue(reply, Customer.class);
            return customer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new customer. The id property is overwritten on the service side.
     * @param customer
     * @return
     */
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

    /**
     * Updates a customer. The id property on the customer body is not used.
     * @param id
     * @param customer
     * @return
     */
    @Put("/{id}")
    public Boolean updateCustomer(@PathVariable UUID id, @Body Customer customer) {
        try {
            Customer reconstructedCustomer = new Customer(id, customer.firstname(), customer.lastname());
            bus.connect();
            String reply = bus.talkSync(exchangeName, "customer.update", mapper.writeValueAsString(reconstructedCustomer));
            LOG.info(reply);
            return mapper.readValue(reply, Boolean.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a customer.
     * @param id
     * @return
     */
    @Delete("/{id}")
    public Boolean deleteCustomer(@PathVariable UUID id) {
        try {
            bus.connect();
            String reply = bus.talkSync(exchangeName, "customer.delete", mapper.writeValueAsString(id));
            LOG.info(reply);
            return mapper.readValue(reply, Boolean.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
