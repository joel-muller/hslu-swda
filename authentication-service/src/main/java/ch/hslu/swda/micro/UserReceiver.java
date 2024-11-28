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

public class UserReceiver implements MessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(UserReceiver.class);
    private final String exchangeName;
    private final BusConnector bus;

    private final AuthStorage authDb = new AuthDatabase();

    public UserReceiver(String exchangeName, BusConnector bus) {
        this.exchangeName = exchangeName;
        this.bus = bus;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        LOG.debug("Received message with routing [{}]", route);
        LOG.info("Received message " + message);
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<UserUpsert> typeRef = new TypeReference<UserUpsert>() {};
        try {
            UserUpsert request = mapper.readValue(message, typeRef);
            UserRole role = authDb.getRoleByName(request.role());
            String passwordHash = BCrypt.hashpw(request.password(), BCrypt.gensalt());
            User user = new User(request.username(), passwordHash, role);
            authDb.addUser(user);

            String logMessage = "User " + user.getUsername() + " with id " + user.getId() + " was created.";
            LogEntry log = new LogEntry("authentication.service",
                    Instant.now().getEpochSecond(),
                    request.employeeId(),
                    "user.create",
                    user.getId(),
                    logMessage);

            String response = mapper.writeValueAsString(User.removeHashForReturn(user));
            bus.reply(exchangeName, replyTo, corrId, response);
            bus.talkAsync(exchangeName, "logs.new", mapper.writeValueAsString(log));
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }
}
