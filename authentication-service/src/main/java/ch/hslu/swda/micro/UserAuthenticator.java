package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.business.AuthDatabase;
import ch.hslu.swda.business.AuthStorage;
import ch.hslu.swda.business.JWTGenerator;
import ch.hslu.swda.entities.LogEntry;
import ch.hslu.swda.entities.SystemRights;
import ch.hslu.swda.entities.User;
import ch.hslu.swda.messages.UserJWT;
import ch.hslu.swda.messages.UserLogin;
import ch.hslu.swda.messages.UserUpsert;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.*;

public class UserAuthenticator implements MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(UserAuthenticator.class);
    private final String exchangeName;
    private final BusConnector bus;

    private final AuthStorage authDb = new AuthDatabase();

    public UserAuthenticator(String exchangeName, BusConnector bus) {
        this.exchangeName = exchangeName;
        this.bus = bus;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        LOG.debug("Received message with routing [{}]", route);
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<UserLogin> typeRef = new TypeReference<>() {};
        try {
            UserLogin request = mapper.readValue(message, typeRef);
            if (request.username() == null || request.username().isEmpty()
                    || request.password() == null || request.password().isEmpty()) {
                LOG.info("Empty username or password");
                bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(new UserJWT("", false)));
                return;
            }
            User user = authDb.getUserByUsername(request.username());
            if (user == null) {
                LOG.info("User not found");
                bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(new UserJWT("", false)));
                return;
            }

            if (BCrypt.checkpw(request.password(), user.getPasswordHash())) {
                String jwt = JWTGenerator.generateJWT(user);
                LogEntry log = new LogEntry("authentication.service",
                        Instant.now().getEpochSecond(),
                        user.getId(),
                        "user.login",
                        user.getId(),
                        "User " + user.getUsername() + " successfully logged in.");
                bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(new UserJWT(jwt, true)));
                bus.talkAsync(exchangeName, "logs.new", mapper.writeValueAsString(log));
            } else {
                LogEntry log = new LogEntry("authentication.service",
                        Instant.now().getEpochSecond(),
                        user.getId(),
                        "user.login",
                        user.getId(),
                        "User " + user.getUsername() + " failed to log in.");
                bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(new UserJWT("", false)));
                bus.talkAsync(exchangeName, "logs.new", mapper.writeValueAsString(log));
            }
        } catch (IOException e) {
            LOG.error(e.getMessage());
        } catch (InvalidKeySpecException e) {
            LOG.error(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            LOG.error(e.getMessage());
        }
    }
}
