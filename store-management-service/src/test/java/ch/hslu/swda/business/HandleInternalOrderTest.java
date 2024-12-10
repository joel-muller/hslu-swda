package ch.hslu.swda.business;

import ch.hslu.swda.messagesIngoing.InternalOrder;
import ch.hslu.swda.messagesOutgoing.InventoryRequest;
import ch.hslu.swda.micro.Service;
import ch.hslu.swda.persistence.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HandleInternalOrderTest {
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
    void handleInternalOrderValidStore() {
        new HandleInternalOrder().modify(databaseConnector, new InternalOrder(storeId, exampleOrders), service);
        assertEquals(new InventoryRequest(orderId, storeId, exampleOrders).storeId(), service.lastInventoryRequest.storeId());
        assertEquals(new InventoryRequest(orderId, storeId, exampleOrders).articles(), service.lastInventoryRequest.articles());
        assertNull(service.lastOrderUpdate);
        assertNull(service.lastLogMessage);
        assertNull(databaseConnector.lastSavedStore);
    }

    @Test
    void handleInternalOrderInvalidStore() {
        new HandleInternalOrder().modify(databaseConnector, new InternalOrder(UUID.randomUUID(), exampleOrders), service);
        assertNull(service.lastInventoryRequest);
        assertNull(service.lastOrderUpdate);
        assertNull(service.lastLogMessage);
        assertNull(databaseConnector.lastSavedStore);
    }
}