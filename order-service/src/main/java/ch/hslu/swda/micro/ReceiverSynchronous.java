package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.business.Modifiable;
import ch.hslu.swda.entities.Order;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.persistence.DatabaseConnector;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ReceiverSynchronous<T extends IngoingMessage> implements MessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(ReceiverSynchronous.class);
    private final DatabaseConnector database;
    private final Modifiable modifiable;
    private final Class<T> messageGenericClass;
    private final Service service;
    private final String exchangeName;
    private final BusConnector bus;


    public ReceiverSynchronous(final DatabaseConnector database, final String exchangeName, final BusConnector bus, final Modifiable modifiable, Class<T> messageGenericClass, Service service) {
        this.database = database;
        this.modifiable = modifiable;
        this.messageGenericClass = messageGenericClass;
        this.service = service;
        this.exchangeName = exchangeName;
        this.bus = bus;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            T response = mapper.readValue(message, messageGenericClass);
            Order order = database.getById(response.getOrderId());
            if (order == null) {
                return;
            }
            if (!order.isCancelled()) {
                modifiable.modify(order, response, service);
            }
            database.storeOrder(order);

            bus.reply(exchangeName, replyTo, corrId, "Request was valid and will be processed");
        } catch (IOException e) {
            LOG.error("Error occurred while mapping the validity reception data: {}", e.getMessage());
        }

        LOG.debug("received chat message with replyTo property [{}]: [{}]", replyTo, message);
        LOG.debug("sending answer with topic [{}] according to replyTo-property", replyTo);
    }
}
