package ch.hslu.swda.business;

import static org.junit.jupiter.api.Assertions.*;

import ch.hslu.swda.entities.Article;
import ch.hslu.swda.entities.Order;
import ch.hslu.swda.entities.StateEnum;
import ch.hslu.swda.messagesIngoing.OrderUpdate;
import ch.hslu.swda.messagesOutgoing.OrderCancelled;
import ch.hslu.swda.messagesOutgoing.OrderReady;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

class UpdateOrderTest {
    Order order;
    UUID id = UUID.randomUUID();
    UUID storeId = UUID.randomUUID();
    UUID customerId = UUID.randomUUID();
    UUID employeeId = UUID.randomUUID();
    Map<Integer, Integer> articlesMap;
    ServiceMock serviceMock;
    Map<Integer, Integer> francsResponse;
    Map<Integer, Integer> centimesResponse;

    @BeforeEach
    void setup() {
        articlesMap = new HashMap<Integer, Integer>();
        articlesMap.put(11, 2);
        articlesMap.put(33, 34);
        articlesMap.put(44, 6);
        articlesMap.put(5, 9);
        List<Article> articles = Article.createListArticle(articlesMap);
        order = new Order(id, Calendar.getInstance().getTime(), storeId, customerId, employeeId, articles, StateEnum.ARTICLE_VALIDATED, false);
        serviceMock = new ServiceMock();
    }

    @Test
    void testUpdateOrder() {
        order.setCustomerValid();
        List<Integer> articles = new ArrayList<>();
        articles.add(11);
        articles.add(33);
        articles.add(44);
        articles.add(5);
        new UpdateOrder().modify(order, new OrderUpdate(order.getId(), articles, true), serviceMock);
        assertEquals(order.getOrderReady(), serviceMock.orderReady);
        assertEquals(order.getInvoice(), serviceMock.invoice);
        assertNull(serviceMock.orderCancelled);
    }

    @Test
    void testUpdateOrderNoValidCustomer() {
        List<Integer> articles = new ArrayList<>();
        articles.add(11);
        articles.add(33);
        articles.add(44);
        articles.add(5);
        new UpdateOrder().modify(order, new OrderUpdate(order.getId(), articles, true), serviceMock);
        assertNull(serviceMock.orderReady);
        assertNull(serviceMock.orderCancelled);
        assertNull(serviceMock.invoice);
    }

    @Test
    void testUpdateOrderNotAllAreValidYet() {
        order.setCustomerValid();
        List<Integer> articles = new ArrayList<>();
        articles.add(11);
        new UpdateOrder().modify(order, new OrderUpdate(order.getId(), articles, true), serviceMock);
        assertNull(serviceMock.orderReady);
        assertNull(serviceMock.orderCancelled);
        assertNull(serviceMock.invoice);
    }

    @Test
    void testEmptyUpdate() {
        order.setCustomerValid();
        List<Integer> articles = new ArrayList<>();
        new UpdateOrder().modify(order, new OrderUpdate(order.getId(), articles, true), serviceMock);
        assertNull(serviceMock.orderReady);
        assertNull(serviceMock.orderCancelled);
        assertNull(serviceMock.invoice);
    }

    @Test
    void testInvalidStore() {
        order.setCustomerValid();
        List<Integer> articles = new ArrayList<>();
        new UpdateOrder().modify(order, new OrderUpdate(order.getId(), articles, false), serviceMock);
        assertNull(serviceMock.orderReady);
        assertEquals(order.getOrderCancelled(), serviceMock.orderCancelled);
        assertNull(serviceMock.invoice);
    }
}