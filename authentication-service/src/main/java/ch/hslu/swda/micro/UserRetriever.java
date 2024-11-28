package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.business.AuthDatabase;
import ch.hslu.swda.business.AuthStorage;
import ch.hslu.swda.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class UserRetriever implements MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(UserRetriever.class);
    private final String exchangeName;
    private final BusConnector bus;

    private final AuthStorage authDb = new AuthDatabase();

    public UserRetriever(String exchangeName, BusConnector bus) {
        this.exchangeName = exchangeName;
        this.bus = bus;
    }
    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        try {
            LOG.debug("Received get request with replyTo [{}]", replyTo);
            ObjectMapper mapper = new ObjectMapper();
            if (!message.isEmpty()) {
                UUID uuid = mapper.readValue(message, UUID.class);
                User user = authDb.getUserById(uuid);
                bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(user));
            } else {
                List<User> list = authDb.getAllUsers();
                LOG.debug("Sending list of log entries with size [{}]", list.size());
                bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(list));
            }
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }
}
