package ch.hslu.swda.business;

import ch.hslu.swda.entities.Store;
import ch.hslu.swda.messagesIngoing.StoreCreation;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.messagesOutgoing.LogMessage;
import ch.hslu.swda.micro.Service;
import ch.hslu.swda.persistence.Data;
import ch.hslu.swda.persistence.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HandleStoreCreation implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(HandleStoreCreation.class);
    @Override
    public void modify(Data databaseConnector, IngoingMessage responseRaw, Service service) {
        try {
            StoreCreation response = (StoreCreation) responseRaw;
            Store store = new Store();
            if (response.addDefaultArticle()) {
                store.addDefaultInventory();
            }
            databaseConnector.storeStore(store);
            service.log(new LogMessage(store.getId(), store.getId(), "store.create", "new store created with the id " + store.getId().toString()));
            LOG.info("Store successfully created with the store id {}", store.getId());
        } catch (IOException e) {
            LOG.error("Exception occurred while trying to create a store {}", e.getMessage());
        }
    }
}
