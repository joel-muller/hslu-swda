package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.RabbitMqConfig;
import ch.hslu.swda.model.customer.Customer;
import ch.hslu.swda.model.user.*;
import io.micronaut.core.type.Argument;
import io.micronaut.http.annotation.*;
import io.micronaut.serde.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * Controller to do CRUD operations on and authenticate users.
 */
@Tag(name = "User")
@Controller("/api/v1/users")
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
    private String exchangeName = new RabbitMqConfig().getExchange();
    private BusConnector bus = new BusConnector();
    @Inject
    private ObjectMapper mapper;

    @Get("/")
    public List<User> getUsers() {
        try {
            bus.connect();
            String reply = bus.talkSync(exchangeName, "user.get", "");
            List<User> list = mapper.readValue(reply, Argument.listOf(User.class));
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Get("/{id}")
    public User getUser(@PathVariable UUID id) {
        try {
            bus.connect();
            String reply = bus.talkSync(exchangeName, "user.get", mapper.writeValueAsString(id));
            User user = mapper.readValue(reply, User.class);
            return user;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Post("/")
    public User createUser(@Body UserUpsert userInsert) {
        try {
            bus.connect();
            String reply = bus.talkSync(exchangeName, "user.create", mapper.writeValueAsString(userInsert));
            User user = mapper.readValue(reply, User.class);
            return user;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Post("/login")
    public UserJWT authenticateUser(@Body UserLogin userLogin) {
        try {
            bus.connect();
            String reply = bus.talkSync(exchangeName, "user.login", mapper.writeValueAsString(userLogin));
            UserJWT jwt = mapper.readValue(reply, UserJWT.class);
            return jwt;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Put("/{id}")
    public boolean updateUser(@PathVariable UUID id, @Body UserUpsert userUpdate) {
        try {
            bus.connect();
            // Setting the upsert explicitly through the path variable.
            UserUpsert updateWithId = new UserUpsert(id, userUpdate.username(), userUpdate.password(), userUpdate.role(), userUpdate.employeeId());
            String reply = bus.talkSync(exchangeName, "user.update", mapper.writeValueAsString(updateWithId));
            boolean success = mapper.readValue(reply, Boolean.class);
            return success;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Delete("/{id}")
    public boolean deleteUser(@PathVariable UUID id, @Body UserDelete userDelete) {
        try {
            bus.connect();
            // Setting the delete operation explicitly through the path variable.
            UserDelete deleteWithId = new UserDelete(id, userDelete.employeeId());
            String reply = bus.talkSync(exchangeName, "user.delete", mapper.writeValueAsString(deleteWithId));
            boolean success = mapper.readValue(reply, Boolean.class);
            return success;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
