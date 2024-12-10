package ch.hslu.swda.business;

import ch.hslu.swda.entities.Store;
import ch.hslu.swda.messagesIngoing.StoreCreation;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.micro.Service;
import ch.hslu.swda.persistence.Data;
import ch.hslu.swda.persistence.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandleStoreCreation implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(HandleStoreCreation.class);
    @Override
    public void modify(Data databaseConnector, IngoingMessage responseRaw, Service service) {
        StoreCreation response = (StoreCreation) responseRaw;
        Store store = new Store();
        if (response.addDefaultArticle()) {
            store.addDefaultInventory();
        }
        databaseConnector.storeStore(store);
        LOG.info("Store successfully created with the store id {}", store.getId());
    }
}
