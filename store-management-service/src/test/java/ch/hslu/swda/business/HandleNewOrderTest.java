package ch.hslu.swda.business;

import ch.hslu.swda.entities.StoreArticle;
import ch.hslu.swda.messagesIngoing.NewOrder;
import ch.hslu.swda.messagesOutgoing.InventoryRequest;
import ch.hslu.swda.messagesOutgoing.OrderUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.resource.New;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class HandleNewOrderTest {
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
    void testStoreNotValid() {
        UUID newOrderId = UUID.randomUUID();
        new HandleNewOrder().modify(databaseConnector, new NewOrder(newOrderId, UUID.randomUUID(), UUID.randomUUID(), new HashMap<>()), service);
        assertNull(service.lastInventoryRequest);
        assertEquals(new OrderUpdate(newOrderId, new ArrayList<>(), false), service.lastOrderUpdate);
        assertNull(service.lastLogMessage);
        assertNull(databaseConnector.lastSavedStore);
    }

    @Test
    void testStoreValidNothingHasToGetReordered() {
        UUID newOrderId = UUID.randomUUID();
        Map<Integer, Integer> orderList = new HashMap<>();
        orderList.put(1, 3);
        orderList.put(2, 5);
        new HandleNewOrder().modify(databaseConnector, new NewOrder(newOrderId, UUID.randomUUID(), storeId, orderList), service);
        assertNull(service.lastInventoryRequest);
        List<Integer> isHere = new ArrayList<>();
        isHere.add(1);
        isHere.add(2);
        assertEquals(new OrderUpdate(newOrderId, isHere, true), service.lastOrderUpdate);
        assertNull(service.lastLogMessage);
        assertEquals(new StoreArticle(1, 7, 5, 10), databaseConnector.lastSavedStore.getCopyOfArticleList().getFirst());
    }

    @Test
    void testStoreValidEverythingHasToGetOrdered() {
        UUID newOrderId = UUID.randomUUID();
        Map<Integer, Integer> orderList = new HashMap<>();
        orderList.put(33, 3);
        orderList.put(44, 5);
        new HandleNewOrder().modify(databaseConnector, new NewOrder(newOrderId, UUID.randomUUID(), storeId, orderList), service);
        Map<Integer, Integer> reorder = new HashMap<>();
        reorder.put(33, 3);
        reorder.put(44, 5);
        assertEquals(new InventoryRequest(newOrderId, storeId, reorder), service.lastInventoryRequest);
        assertNull(service.lastOrderUpdate);
        assertNull(service.lastLogMessage);
        assertEquals(new StoreArticle(1, 10, 5, 10), databaseConnector.lastSavedStore.getCopyOfArticleList().getFirst());
    }

    @Test
    void testStoreValidSomethingHasToGetReorderedSomethingIsHere() {
        UUID newOrderId = UUID.randomUUID();
        Map<Integer, Integer> orderList = new HashMap<>();
        orderList.put(33, 3);
        orderList.put(1, 2);
        new HandleNewOrder().modify(databaseConnector, new NewOrder(newOrderId, UUID.randomUUID(), storeId, orderList), service);
        Map<Integer, Integer> reorder = new HashMap<>();
        reorder.put(33, 3);
        List<Integer> isHere = new ArrayList<>();
        isHere.add(1);
        assertEquals(new InventoryRequest(newOrderId, storeId, reorder), service.lastInventoryRequest);
        assertEquals(new OrderUpdate(newOrderId, isHere, true), service.lastOrderUpdate);
        assertNull(service.lastLogMessage);
        assertEquals(new StoreArticle(1, 8, 5, 10), databaseConnector.lastSavedStore.getCopyOfArticleList().getFirst());
    }
}