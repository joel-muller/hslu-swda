package ch.hslu.swda.micro;

import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.business.DatabaseConnector;
import ch.hslu.swda.business.Modifiable;
import ch.hslu.swda.entities.Store;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Receiver<T extends IngoingMessage> implements MessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(Receiver.class);
    private final DatabaseConnector database;
    private final Modifiable modifiable;
    private final Class<T> messageGenericClass;
    private final Service service;


    public Receiver(final DatabaseConnector database, final Modifiable modifiable, Class<T> messageGenericClass, Service service) {
        this.database = database;
        this.modifiable = modifiable;
        this.messageGenericClass = messageGenericClass;
        this.service = service;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            T response = mapper.readValue(message, messageGenericClass);
            Store store = database.getStoreById(response.getStoreId());
            store.modify(modifiable, response, service, database);
            database.saveStoreObject(store);
        } catch (IOException e) {
            LOG.error("Error occurred while mapping the validity reception data: {}", e.getMessage());
        }

        LOG.debug("received chat message with replyTo property [{}]: [{}]", replyTo, message);
        LOG.debug("sending answer with topic [{}] according to replyTo-property", replyTo);
    }
}
