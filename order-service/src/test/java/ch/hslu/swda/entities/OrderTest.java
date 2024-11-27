package ch.hslu.swda.entities;

import static org.junit.jupiter.api.Assertions.*;

import ch.hslu.swda.messagesOutgoing.CustomerRequest;
import ch.hslu.swda.messagesOutgoing.StoreRequest;
import ch.hslu.swda.messagesOutgoing.VerifyRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

class OrderTest {
    private Order order;
    private UUID orderId;
    private UUID storeId;
    private UUID customerId;
    private UUID employeeId;
    private List<Article> articles;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        storeId = UUID.randomUUID();
        customerId = UUID.randomUUID();
        employeeId = UUID.randomUUID();
        articles = List.of(new Article(1, 5), new Article(2, 10));
        order = new Order(orderId, articles, storeId, customerId, employeeId);
    }

    @Test
    void testGetId() {
        assertEquals(orderId, order.getId(), "getId should return the correct UUID of the order");
    }

    @Test
    void testGetState() {
        assertNotNull(order.getState(), "getState should return a non-null State object");
    }

    @Test
    void testGetArticles() {
        assertEquals(articles, order.getArticles(), "getArticles should return the correct list of articles");
    }

    @Test
    void testSetArticles() {
        List<Article> newArticles = List.of(new Article(3, 15));
        order.setArticles(newArticles);
        assertEquals(newArticles, order.getArticles(), "setArticles should update the articles list");
    }

    @Test
    void testGetDate() {
        assertNotNull(order.getDate(), "getDate should return a non-null Date object");
    }

    @Test
    void testSetDate() {
        Date newDate = new Date();
        order.setDate(newDate);
        assertEquals(newDate, order.getDate(), "setDate should update the date");
    }

    @Test
    void testGetStoreId() {
        assertEquals(storeId, order.getStoreId(), "getStoreId should return the correct store UUID");
    }

    @Test
    void testSetStoreId() {
        UUID newStoreId = UUID.randomUUID();
        order.setStoreId(newStoreId);
        assertEquals(newStoreId, order.getStoreId(), "setStoreId should update the store UUID");
    }

    @Test
    void testGetCustomerId() {
        assertEquals(customerId, order.getCustomerId(), "getCustomerId should return the correct customer UUID");
    }

    @Test
    void testSetCustomerId() {
        UUID newCustomerId = UUID.randomUUID();
        order.setCustomerId(newCustomerId);
        assertEquals(newCustomerId, order.getCustomerId(), "setCustomerId should update the customer UUID");
    }

    @Test
    void testGetEmployeeId() {
        assertEquals(employeeId, order.getEmployeeId(), "getEmployeeId should return the correct employee UUID");
    }

    @Test
    void testSetEmployeeId() {
        UUID newEmployeeId = UUID.randomUUID();
        order.setEmployeeId(newEmployeeId);
        assertEquals(newEmployeeId, order.getEmployeeId(), "setEmployeeId should update the employee UUID");
    }

    @Test
    void testCreateMapOfArticles() {
        Map<Integer, Integer> expectedMap = new HashMap<>();
        expectedMap.put(1, 5);
        expectedMap.put(2, 10);

        Map<Integer, Integer> result = order.createMapOfArticles();
        assertEquals(expectedMap, result, "createMapOfArticles should return a map of article IDs and counts");
    }

    @Test
    void testGetVerifyRequest() {
        VerifyRequest verifyRequest = order.getVerifyRequest();
        assertNotNull(verifyRequest, "getVerifyRequest should return a non-null VerifyRequest");
        assertEquals(order.getId(), verifyRequest.getOrderId(), "VerifyRequest should contain the correct order ID");
        assertEquals(order.getEmployeeId(), verifyRequest.getEmployeeId(), "VerifyRequest should contain the correct employee ID");
    }

    @Test
    void testGetStoreRequest() {
        StoreRequest storeRequest = order.getStoreRequest();
        assertNotNull(storeRequest, "getStoreRequest should return a non-null StoreRequest");
        assertEquals(order.getId(), storeRequest.getOrderId(), "StoreRequest should contain the correct order ID");
        assertEquals(order.getEmployeeId(), storeRequest.getEmployeeId(), "StoreRequest should contain the correct employee ID");
        assertEquals(order.getArticles().size(), storeRequest.getArticles().size(), "Not all articles where given with the store request");
    }

    @Test
    void testGetCustomerRequest() {
        CustomerRequest customerRequest = order.getCustomerRequest();
        assertNotNull(customerRequest, "getCustomerRequest should return a non-null CustomerRequest");
        assertEquals(order.getId(), customerRequest.orderId(), "CustomerRequest should contain the correct customer ID");
        assertEquals(order.getEmployeeId(), customerRequest.employeeId(), "CustomerRequest should contain the correct employee ID");
        assertEquals(order.getCustomerId(), customerRequest.customerId(), "CustomerRequest should contain the correct order ID");
    }


    @Test
    void testEquals() {
        Order sameOrder = new Order(orderId, articles, storeId, customerId, employeeId);
        Order differentOrder = new Order(UUID.randomUUID(), articles, storeId, customerId, employeeId);

        assertEquals(order, sameOrder, "Orders with the same ID should be equal");
        assertNotEquals(order, differentOrder, "Orders with different IDs should not be equal");
    }

    @Test
    void testHashCode() {
        Order sameOrder = new Order(orderId, articles, storeId, customerId, employeeId);
        assertEquals(order.hashCode(), sameOrder.hashCode(), "Orders with the same ID should have the same hash code");

        Order differentOrder = new Order(UUID.randomUUID(), articles, storeId, customerId, employeeId);
        assertNotEquals(order.hashCode(), differentOrder.hashCode(), "Orders with different IDs should have different hash codes");
    }

    @Test
    void testToString() {
        String expectedString = "Order{" +
                "id=" + orderId +
                ", state=" + order.getState() +
                ", articles=" + articles +
                ", date=" + order.getDate() +
                ", storeId=" + storeId +
                ", customerId=" + customerId +
                ", employeeId=" + employeeId +
                '}';

        assertEquals(expectedString, order.toString(), "toString should match the expected format");
    }

}