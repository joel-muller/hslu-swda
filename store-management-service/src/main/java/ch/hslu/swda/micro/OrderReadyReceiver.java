package ch.hslu.swda.micro;

import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.business.DatabaseConnector;
import ch.hslu.swda.business.ProcessOrderReady;
import ch.hslu.swda.entities.Store;
import ch.hslu.swda.messages.OrderReady;
import ch.hslu.swda.messages.OrderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class OrderReadyReceiver implements MessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(OrderReadyReceiver.class);

    private final DatabaseConnector database;
    private final Service service;

    public OrderReadyReceiver(final DatabaseConnector database, final Service service) {
        this.database = database;
        this.service = service;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            OrderReady request = mapper.readValue(message, OrderReady.class);
            Store store = database.getStoreById(request.storeId());
            store.modify(new ProcessOrderReady(this.service, this.database, request));
        } catch (IOException e) {
            LOG.error("Error occurred while trying to finish order {}", e.getMessage());
        }
    }
}
