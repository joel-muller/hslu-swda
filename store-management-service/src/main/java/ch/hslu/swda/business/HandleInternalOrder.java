package ch.hslu.swda.business;

import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.messagesIngoing.StoreInternalOrder;
import ch.hslu.swda.messagesOutgoing.WarehouseRequest;
import ch.hslu.swda.messagesOutgoing.LogMessage;
import ch.hslu.swda.micro.Service;
import ch.hslu.swda.persistence.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

public class HandleInternalOrder implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(HandleInternalOrder.class);

    @Override
    public void modify(Data databaseConnector, IngoingMessage responseRaw, Service service) {
        try {
            StoreInternalOrder request = (StoreInternalOrder) responseRaw;
            if (databaseConnector.getStore(request.getStoreId()) == null) return;
            UUID orderId = UUID.randomUUID();
            service.requestArticles(new WarehouseRequest(orderId, request.getStoreId(), request.articles()));
            service.log(new LogMessage(orderId, request.getStoreId(), "store.internalOrder", "Internal order created for the store with the id " + request.storeId().toString()));
            LOG.info("Inventory store update {}", responseRaw.toString());
        } catch (IOException e) {
            LOG.error("An exception occurred while trying to send a request to the central warehouse {}", e.getMessage());
        }
        LOG.info("Internal store order modifier {}", responseRaw.toString());
    }
}
