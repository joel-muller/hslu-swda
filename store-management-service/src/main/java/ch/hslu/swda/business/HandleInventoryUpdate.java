package ch.hslu.swda.business;

import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.micro.Service;
import ch.hslu.swda.persistence.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandleInventoryUpdate implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(HandleInventoryUpdate.class);


    @Override
    public void modify(DatabaseConnector databaseConnector, IngoingMessage responseRaw, Service service) {
        LOG.info("Inventory store update {}", responseRaw.toString());
    }
}
