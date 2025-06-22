package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.business.AuthDatabase;
import ch.hslu.swda.business.AuthStorage;
import ch.hslu.swda.entities.LogEntry;
import ch.hslu.swda.entities.User;
import ch.hslu.swda.messages.UserDelete;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

public class UserDeleter implements MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(UserDeleter.class);
    private final String exchangeName;
    private final BusConnector bus;

    private final AuthStorage authDb = new AuthDatabase();

    public UserDeleter(String exchangeName, BusConnector bus) {
        this.exchangeName = exchangeName;
        this.bus = bus;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        try {
            LOG.debug("Received validity request with replyTo [{}]", replyTo);
            ObjectMapper mapper = new ObjectMapper();
            UserDelete request = mapper.readValue(message, UserDelete.class);
            if (request.userId().equals(request.employeeId())) {
                LOG.error("User and Employee cannot be the same.");
                bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(false));
                return;
            }
            User storedUser = authDb.getUserById(request.userId());
            LogEntry logEntry;
            if (storedUser == null) {
                logEntry = new LogEntry("authentication.service",
                        Instant.now().getEpochSecond(),
                        request.employeeId(),
                        "user.delete",
                        request.userId(),
                        "Non-existing user of id " + request.userId() + " attempted to be deleted.");
            } else {
                logEntry = new LogEntry("authentication.service",
                        Instant.now().getEpochSecond(),
                        request.employeeId(),
                        "user.delete",
                        request.userId(),
                        "User of id " + request.userId() + " was deleted.");
            }
            boolean success = authDb.deleteUser(request.userId());
            bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(success));
            bus.talkAsync(exchangeName, "logs.new", mapper.writeValueAsString(logEntry));
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }
}
