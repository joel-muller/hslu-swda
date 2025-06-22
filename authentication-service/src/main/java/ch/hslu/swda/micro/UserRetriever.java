package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.business.AuthDatabase;
import ch.hslu.swda.business.AuthStorage;
import ch.hslu.swda.entities.User;
import ch.hslu.swda.entities.UserSimple;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
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
                UserSimple simpleUser = new UserSimple(user.getId(), user.getUsername(), user.getRole().getName());
                bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(simpleUser));
            } else {
                List<User> list = authDb.getAllUsers();
                List<UserSimple> simpleList = new ArrayList<>();
                for (User user : list) {
                    simpleList.add(new UserSimple(user.getId(), user.getUsername(), user.getRole().getName()));
                }
                LOG.debug("Sending list of log entries with size [{}]", list.size());
                bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(simpleList));
            }
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }
}
