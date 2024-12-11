package ch.hslu.swda.business;

import ch.hslu.swda.entities.StoreArticle;
import ch.hslu.swda.messagesIngoing.OrderCancelled;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HandleOrderCancelledTest {
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
        new HandleOrderCancelled().modify(databaseConnector, new OrderCancelled(databaseConnector.orderId, storeId), service);
        assertEquals(0, databaseConnector.lastSavedStore.getCopyOfOpenOrders().size());
        assertNull(service.lastOrderUpdate);
        //assertNull(service.lastLogMessage);
        assertNull(service.lastInventoryRequest);
        assertEquals(new StoreArticle(3, 100, 0, 0), databaseConnector.lastSavedStore.getCopyOfArticleList().get(2));
    }

    @Test
    void cancelOrderStoreNotExisting() {
        new HandleOrderCancelled().modify(databaseConnector, new OrderCancelled(databaseConnector.orderId, UUID.randomUUID()), service);
        assertNull(databaseConnector.lastSavedStore);
        assertNull(service.lastOrderUpdate);
        //assertNull(service.lastLogMessage);
        assertNull(service.lastInventoryRequest);
    }

    @Test
    void cancelOrderOrderNotExisting() {
        new HandleOrderCancelled().modify(databaseConnector, new OrderCancelled(UUID.randomUUID(), storeId), service);
        assertEquals(databaseConnector.getStore(storeId), databaseConnector.lastSavedStore);
        assertNull(service.lastOrderUpdate);
        //assertNull(service.lastLogMessage);
        assertNull(service.lastInventoryRequest);
    }

}