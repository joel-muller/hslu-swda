package ch.hslu.swda.entities;

import static org.junit.jupiter.api.Assertions.*;

import ch.hslu.swda.messagesIngoing.OrderUpdate;
import ch.hslu.swda.messagesIngoing.OrderReceiveValidity;
import ch.hslu.swda.messagesOutgoing.CustomerValidate;
import ch.hslu.swda.messagesOutgoing.InvoiceCreate;
import ch.hslu.swda.messagesOutgoing.StoreRequestArticles;
import ch.hslu.swda.messagesOutgoing.ArticleCheckValidity;
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
    private StateEnum stateEnum;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        storeId = UUID.randomUUID();
        customerId = UUID.randomUUID();
        employeeId = UUID.randomUUID();
        stateEnum = StateEnum.STORED;
        articles = List.of(new Article(1, 5, new Price(15, 10)), new Article(2, 10, new Price(4, 50)));
        order = new Order(orderId, Calendar.getInstance().getTime(), storeId, customerId, employeeId, articles, stateEnum, false);
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
        assertEquals(articles, order.getCopyOfArticles(), "getArticles should return the correct list of articles");
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
        ArticleCheckValidity articleCheckValidity = order.getVerifyRequest();
        assertNotNull(articleCheckValidity, "getVerifyRequest should return a non-null VerifyRequest");
        assertEquals(order.getId(), articleCheckValidity.orderId(), "VerifyRequest should contain the correct order ID");
        assertEquals(order.getEmployeeId(), articleCheckValidity.employeeId(), "VerifyRequest should contain the correct employee ID");
    }

    @Test
    void testGetStoreRequest() {
        StoreRequestArticles storeRequestArticles = order.getStoreRequest();
        assertNotNull(storeRequestArticles, "getStoreRequest should return a non-null StoreRequest");
        assertEquals(order.getId(), storeRequestArticles.getOrderId(), "StoreRequest should contain the correct order ID");
        assertEquals(order.getEmployeeId(), storeRequestArticles.getEmployeeId(), "StoreRequest should contain the correct employee ID");
        assertEquals(order.getCopyOfArticles().size(), storeRequestArticles.getArticles().size(), "Not all articles where given with the store request");
    }

    @Test
    void testGetCustomerRequest() {
        CustomerValidate customerValidate = order.getCustomerRequest();
        assertNotNull(customerValidate, "getCustomerRequest should return a non-null CustomerRequest");
        assertEquals(order.getId(), customerValidate.orderId(), "CustomerRequest should contain the correct customer ID");
        assertEquals(order.getEmployeeId(), customerValidate.employeeId(), "CustomerRequest should contain the correct employee ID");
        assertEquals(order.getCustomerId(), customerValidate.customerId(), "CustomerRequest should contain the correct order ID");
    }


    @Test
    void testEquals() {
        Order sameOrder = new Order(orderId, Calendar.getInstance().getTime(), storeId, customerId, employeeId, articles, StateEnum.STORED, false);
        Order differentOrder = new Order(UUID.randomUUID(), Calendar.getInstance().getTime(), storeId, customerId, employeeId, articles, StateEnum.STORED, false);

        assertEquals(order, sameOrder, "Orders with the same ID should be equal");
        assertNotEquals(order, differentOrder, "Orders with different IDs should not be equal");
    }

    @Test
    void testHashCode() {
        Order sameOrder = new Order(orderId, Calendar.getInstance().getTime(), storeId, customerId, employeeId, articles, StateEnum.STORED, false);
        Order differentOrder = new Order(UUID.randomUUID(), Calendar.getInstance().getTime(), storeId, customerId, employeeId, articles, StateEnum.STORED, false);
        assertEquals(order.hashCode(), sameOrder.hashCode(), "Orders with the same ID should have the same hash code");
        assertNotEquals(order.hashCode(), differentOrder.hashCode(), "Orders with different IDs should have different hash codes");
    }

    @Test
    void testToString() {
        String expectedString = "Order{" +
                "id=" + orderId +
                ", state=" + stateEnum.toString() +
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
                .withIgnoredFields("date", "storeId", "customerId", "employeeId", "stateEnum", "articles", "cancelled")
                .verify();
    }

    @Test
    void testSetArticleInStore() {
        order.setArticleInStore(1);
        order.setArticleInStore(1);
        Article article = order.getCopyOfArticles().getFirst();
        assertTrue(article.isDelivered(), "article is not delivered " + article.toString());
        assertFalse(order.allArticlesDelivered());
        order.setArticleInStore(2);
        assertTrue(order.allArticlesDelivered());
    }

    @Test
    void testHandleVerifyResponseInvalidResponse() {
        OrderReceiveValidity response = new OrderReceiveValidity(orderId, false, new HashMap<>(), new HashMap<>());
        order.handleVerifyResponse(response);
        assertTrue(order.isCancelled());
        assertFalse(order.isArticlesValid());
    }

    @Test
    void testHandleVerifyResponseNotAllArticlesHere() {
        Map<Integer, Integer> francsResponse = new HashMap<>();
        francsResponse.put(1, 4);
        Map<Integer, Integer> centimesResponse = new HashMap<>();
        centimesResponse.put(1, 10);
        OrderReceiveValidity response = new OrderReceiveValidity(orderId, true, francsResponse, centimesResponse);
        order.handleVerifyResponse(response);
        assertTrue(order.isCancelled());
        assertFalse(order.isArticlesValid());
    }

    @Test
    void testHandleVerifyResponse() {
        Map<Integer, Integer> francsResponse = new HashMap<>();
        francsResponse.put(1, 4);
        francsResponse.put(2, 5);
        Map<Integer, Integer> centimesResponse = new HashMap<>();
        centimesResponse.put(1, 10);
        centimesResponse.put(2, 50);
        OrderReceiveValidity response = new OrderReceiveValidity(orderId, true, francsResponse, centimesResponse);
        order.handleVerifyResponse(response);
        assertFalse(order.isCancelled());
        assertTrue(order.isArticlesValid());
        assertEquals(new Price(4, 10), order.getCopyOfArticles().getFirst().getPrice());
        assertEquals(new Price(5, 50), order.getCopyOfArticles().getLast().getPrice());
        assertEquals(5, order.getCopyOfArticles().getFirst().getCount());
        assertEquals(10, order.getCopyOfArticles().getLast().getCount());
        assertEquals(new Price(75, 50), order.getTotalPrice());
    }

    @Test
    void testHandleOrderUpdateOnlyOne() {
        List<Integer> ready = new ArrayList<>();
        ready.add(1);
        OrderUpdate update = new OrderUpdate(orderId, ready, true);
        order.handleOrderUpdate(update);
        List<Article> articleList = order.getCopyOfArticles();
        Article article = articleList.getFirst();
        assertTrue(article.isDelivered());
        assertFalse(order.isArticleReady());
        assertFalse(order.isCancelled());
    }

    @Test
    void testHandleOrderUpdateBoth() {
        List<Integer> ready = new ArrayList<>();
        ready.add(1);
        ready.add(2);
        OrderUpdate update = new OrderUpdate(orderId, ready, true);
        order.handleOrderUpdate(update);
        List<Article> articleList = order.getCopyOfArticles();
        Article article = articleList.getFirst();
        assertTrue(article.isDelivered());
        article = articleList.getLast();
        assertTrue(article.isDelivered());
        assertTrue(order.isArticleReady());
        assertFalse(order.isCancelled());
    }

    @Test
    void testHandleOrderInvalidStore() {
        List<Integer> ready = new ArrayList<>();
        ready.add(1);
        ready.add(2);
        OrderUpdate update = new OrderUpdate(orderId, ready, false);
        order.handleOrderUpdate(update);
        List<Article> articleList = order.getCopyOfArticles();
        Article article = articleList.getFirst();
        assertFalse(article.isDelivered());
        article = articleList.getLast();
        assertFalse(article.isDelivered());
        assertFalse(order.isArticleReady());
        assertTrue(order.isCancelled());
    }

    @Test
    void testIsCancelled() {
        assertFalse(order.isCancelled());
        order.setCancelled();
        assertTrue(order.isCancelled());
    }

    @Test
    void tesGetTotalPrice() {
        assertEquals(120, order.getTotalPrice().getFrancs());
        assertEquals(50, order.getTotalPrice().getCentimes());
    }

    @Test
    void getInvoice() {
        Map<Integer, Integer> count = new HashMap<>();
        Map<Integer, String> prices = new HashMap<>();
        count.put(1, 5);
        count.put(2, 10);
        prices.put(1, "75.50");
        prices.put(2, "45.00");
        assertEquals(new InvoiceCreate(orderId, customerId, employeeId, storeId, count, prices, order.getTotalPrice().getInvoiceString()), order.getInvoice());
    }
}