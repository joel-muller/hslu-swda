package ch.hslu.swda.business;

import ch.hslu.swda.entities.Store;
import ch.hslu.swda.messagesIngoing.StoreCreation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HandleStoreCreationTest {
    UUID storeIdExistingStore;
    UUID existingOrderId;
    FakeDatabaseConnector databaseConnector;
    FakeService service;

    @BeforeEach
    void setup() {
        storeIdExistingStore = UUID.randomUUID();
        existingOrderId = UUID.randomUUID();
        databaseConnector = new FakeDatabaseConnector(storeIdExistingStore, existingOrderId);
        service = new FakeService();
    }

    @Test
    void createNormalStore() {
        new HandleStoreCreation().modify(databaseConnector, new StoreCreation(false), service);
        assertEquals(0, databaseConnector.lastSavedStore.getCopyOfArticleList().size());
        assertEquals(0, databaseConnector.lastSavedStore.getCopyOfOpenOrders().size());
    }

    @Test
    void createFilledUpStore() {
        new HandleStoreCreation().modify(databaseConnector, new StoreCreation(true), service);
        assertEquals(100, databaseConnector.lastSavedStore.getCopyOfArticleList().size());
        assertEquals(0, databaseConnector.lastSavedStore.getCopyOfOpenOrders().size());
    }
}