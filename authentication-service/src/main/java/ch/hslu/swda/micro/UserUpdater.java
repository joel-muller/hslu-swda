package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.business.AuthDatabase;
import ch.hslu.swda.business.AuthStorage;
import ch.hslu.swda.entities.LogEntry;
import ch.hslu.swda.entities.User;
import ch.hslu.swda.entities.UserRole;
import ch.hslu.swda.messages.UserUpsert;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;

public class UserUpdater implements MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(UserUpdater.class);
    private final String exchangeName;
    private final BusConnector bus;

    private final AuthStorage authDb = new AuthDatabase();

    public UserUpdater(String exchangeName, BusConnector bus) {
        this.exchangeName = exchangeName;
        this.bus = bus;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        LOG.debug("Received message with routing [{}]", route);
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<UserUpsert> typeRef = new TypeReference<UserUpsert>() {};
        try {
            UserUpsert request = mapper.readValue(message, typeRef);
            if (request.id() == null) {
                LOG.error("User not found");
                bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(null));
                return;
            }
            User oldUser = authDb.getUserById(request.id());
            boolean success;
            if (request.role() != null || !request.role().isEmpty()) {
                UserRole role = authDb.getRoleByName(request.role());
                success = authDb.updateUser(request.id(), request.username(), request.password(), role);
            } else {
                success = authDb.updateUser(request.id(), request.username(), request.password(), null);
            }

            if (!success) {
                bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(false));
                LOG.info("User of id [{}] and username [{}] could not be updated or fully updated.", oldUser.getId(), oldUser.getUsername());
                return;
            }
            User updatedUser = User.removeHashForReturn(authDb.getUserById(request.id()));
            String logMessage = "User " + oldUser.getUsername() + " with id " + oldUser.getId() + " was updated. Current state: " + updatedUser;
            LogEntry log = new LogEntry("authentication.service",
                    Instant.now().getEpochSecond(),
                    request.employeeId(),
                    "user.update",
                    request.id(),
                    logMessage);

            bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(true));
            bus.talkAsync(exchangeName, "logs.new", mapper.writeValueAsString(log));
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }
}
