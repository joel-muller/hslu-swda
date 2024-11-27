package ch.hslu.swda.business;

import ch.hslu.swda.entities.Store;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.micro.Service;
import ch.hslu.swda.persistence.DatabaseConnector;

public interface Modifiable {
    void modify(Store store, IngoingMessage responseRaw, Service service);
}
