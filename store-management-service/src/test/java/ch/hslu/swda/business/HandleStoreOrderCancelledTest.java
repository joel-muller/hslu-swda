package ch.hslu.swda.business;

import ch.hslu.swda.entities.StoreArticle;
import ch.hslu.swda.messagesIngoing.StoreOrderCancelled;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HandleStoreOrderCancelledTest {
    UUID storeId;
    UUID existingOrderId;
    FakeDatabaseConnector databaseConnector;
    FakeService service;

    @BeforeEach
    void setup() {
        storeId = UUID.randomUUID();
        existingOrderId = UUID.randomUUID();
        databaseConnector = new FakeDatabaseConnector(storeId, existingOrderId);
        service = new FakeService();
    }

    @Test
    void cancelOrder() {
        new HandleOrderCancelled().modify(databaseConnector, new StoreOrderCancelled(databaseConnector.orderId, storeId), service);
        assertEquals(0, databaseConnector.lastSavedStore.getCopyOfOpenOrders().size());
        assertNull(service.lastOrderUpdate);
        //assertNull(service.lastLogMessage);
        assertNull(service.lastWarehouseRequest);
        assertEquals(new StoreArticle(3, 100, 0, 0), databaseConnector.lastSavedStore.getCopyOfArticleList().get(2));
    }

    @Test
    void cancelOrderStoreNotExisting() {
        new HandleOrderCancelled().modify(databaseConnector, new StoreOrderCancelled(databaseConnector.orderId, UUID.randomUUID()), service);
        assertNull(databaseConnector.lastSavedStore);
        assertNull(service.lastOrderUpdate);
        //assertNull(service.lastLogMessage);
        assertNull(service.lastWarehouseRequest);
    }

    @Test
    void cancelOrderOrderNotExisting() {
        new HandleOrderCancelled().modify(databaseConnector, new StoreOrderCancelled(UUID.randomUUID(), storeId), service);
        assertEquals(databaseConnector.getStore(storeId), databaseConnector.lastSavedStore);
        assertNull(service.lastOrderUpdate);
        //assertNull(service.lastLogMessage);
        assertNull(service.lastWarehouseRequest);
    }

}