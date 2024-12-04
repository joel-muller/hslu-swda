package ch.hslu.swda.entities;

import static org.junit.jupiter.api.Assertions.*;

import ch.hslu.swda.messagesIngoing.VerifyResponse;
import ch.hslu.swda.messagesOutgoing.CustomerRequest;
import ch.hslu.swda.messagesOutgoing.StoreRequest;
import ch.hslu.swda.messagesOutgoing.VerifyRequest;
import nl.jqno.equalsverifier.EqualsVerifier;
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
        articles = List.of(new Article(1, 5, new Price(15, 10)), new Article(2, 10, new Price(4, 50)));
        order = new Order(orderId, Calendar.getInstance().getTime(), storeId, customerId, employeeId, new State(), articles);
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
    void testGetStoreId() {
        assertEquals(storeId, order.getStoreId(), "getStoreId should return the correct store UUID");
    }


    @Test
    void testGetCustomerId() {
        assertEquals(customerId, order.getCustomerId(), "getCustomerId should return the correct customer UUID");
    }


    @Test
    void testGetEmployeeId() {
        assertEquals(employeeId, order.getEmployeeId(), "getEmployeeId should return the correct employee UUID");
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
        Order sameOrder = new Order(orderId, Calendar.getInstance().getTime(), storeId, customerId, employeeId, new State(), articles);
        Order differentOrder = new Order(UUID.randomUUID(), Calendar.getInstance().getTime(), storeId, customerId, employeeId, new State(), articles);

        assertEquals(order, sameOrder, "Orders with the same ID should be equal");
        assertNotEquals(order, differentOrder, "Orders with different IDs should not be equal");
    }

    @Test
    void testHashCode() {
        Order sameOrder = new Order(orderId, Calendar.getInstance().getTime(), storeId, customerId, employeeId, new State(), articles);
        Order differentOrder = new Order(UUID.randomUUID(), Calendar.getInstance().getTime(), storeId, customerId, employeeId, new State(), articles);
        assertEquals(order.hashCode(), sameOrder.hashCode(), "Orders with the same ID should have the same hash code");
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

    @Test
    void testWithEqualsVerifier() {
        EqualsVerifier.simple().forClass(Order.class)
                .withIgnoredFields("date", "storeId", "customerId", "employeeId", "state", "articles")
                .verify();
    }

    @Test
    void testSetArticleInStore() {
        order.setArticleInStore(1);
        order.setArticleInStore(1);
        assertFalse(order.allArticlesDelivered());
        order.setArticleInStore(2);
        assertTrue(order.allArticlesDelivered());
    }

    @Test
    void testHandleVerifyResponseInvalidResponse() {
        VerifyResponse response = new VerifyResponse(orderId, false, new HashMap<>(), new HashMap<>());
        order.handleVerifyResponse(response);
        assertTrue(order.isCancelled());
        assertFalse(order.getState().isValid());
    }

    @Test
    void testHandleVerifyResponseNotAllArticlesHere() {
        Map<Integer, Integer> francsResponse = new HashMap<>();
        francsResponse.put(1, 4);
        Map<Integer, Integer> centimesResponse = new HashMap<>();
        centimesResponse.put(1, 10);
        VerifyResponse response = new VerifyResponse(orderId, true, francsResponse, centimesResponse);
        order.handleVerifyResponse(response);
        assertTrue(order.isCancelled());
        assertFalse(order.getState().isValid());
    }

    @Test
    void testHandleVerifyResponse() {
        Map<Integer, Integer> francsResponse = new HashMap<>();
        francsResponse.put(1, 4);
        francsResponse.put(2, 5);
        Map<Integer, Integer> centimesResponse = new HashMap<>();
        centimesResponse.put(1, 10);
        centimesResponse.put(2, 50);
        VerifyResponse response = new VerifyResponse(orderId, true, francsResponse, centimesResponse);
        order.handleVerifyResponse(response);
        assertFalse(order.isCancelled());
        assertTrue(order.getState().isValid());
        assertEquals(new Price(9, 60), order.getTotalPrice());
    }

    @Test
    void testIsCancelled() {
        assertFalse(order.isCancelled());
        order.setCancelled();
        assertTrue(order.isCancelled());
    }

    @Test
    void tesGetTotalPrice() {
        assertEquals(19, order.getTotalPrice().getFrancs());
        assertEquals(60, order.getTotalPrice().getCentimes());
    }

}