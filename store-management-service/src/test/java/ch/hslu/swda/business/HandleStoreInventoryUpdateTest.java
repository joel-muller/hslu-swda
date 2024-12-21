package ch.hslu.swda.business;

import ch.hslu.swda.entities.StoreArticle;
import ch.hslu.swda.messagesIngoing.StoreInventoryUpdate;
import ch.hslu.swda.messagesOutgoing.OrderUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class HandleStoreInventoryUpdateTest {
    UUID storeId;
    UUID orderId;
    FakeDatabaseConnector databaseConnector;
    FakeService service;
    Map<Integer, Integer> exampleOrders;

    @BeforeEach
    void setup() {
        storeId = UUID.randomUUID();
        orderId = UUID.randomUUID();
        databaseConnector = new FakeDatabaseConnector(storeId, orderId);
        service = new FakeService();
        exampleOrders = new HashMap<>();
        exampleOrders.put(1, 44);
        exampleOrders.put(3, 48);
        exampleOrders.put(4, 70);
    }

    @Test
    void testStoreDoesNotExist() {
        new HandleInventoryUpdate().modify(databaseConnector, new StoreInventoryUpdate(exampleOrders, orderId, UUID.randomUUID()), service);
        assertNull(service.lastWarehouseRequest);
        //assertNull(service.lastLogMessage);
        assertNull(service.lastOrderUpdate);
    }

    @Test
    void testValidStoreOrderReady() {
        Map<Integer, Integer> articles = new HashMap<>();
        articles.put(4, 60);
        List<Integer> update = new ArrayList<>();
        update.add(4);
        new HandleInventoryUpdate().modify(databaseConnector, new StoreInventoryUpdate(articles, orderId, storeId), service);
        assertNull(service.lastWarehouseRequest);
        //assertNull(service.lastLogMessage);
        assertEquals(new OrderUpdate(orderId, update, true), service.lastOrderUpdate);
        assertEquals(new StoreArticle(4, 56, 0, 0), databaseConnector.lastSavedStore.getCopyOfArticleList().get(3));
    }

    @Test
    void testValidStoreNothingToUpdate() {
        Map<Integer, Integer> articles = new HashMap<>();
        articles.put(33, 60);
        new HandleInventoryUpdate().modify(databaseConnector, new StoreInventoryUpdate(articles, orderId, storeId), service);
        assertNull(service.lastWarehouseRequest);
        //assertNull(service.lastLogMessage);
        assertNull(service.lastOrderUpdate);
        assertEquals(new StoreArticle(33, 60, 0, 0), databaseConnector.lastSavedStore.getCopyOfArticleList().get(3));
    }
}