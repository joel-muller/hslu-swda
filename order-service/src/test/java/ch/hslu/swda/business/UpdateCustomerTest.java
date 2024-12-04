package ch.hslu.swda.business;

import ch.hslu.swda.entities.Article;
import ch.hslu.swda.entities.Order;
import ch.hslu.swda.entities.State;
import ch.hslu.swda.messagesIngoing.CustomerResponse;
import ch.hslu.swda.messagesOutgoing.OrderCancelled;
import ch.hslu.swda.messagesOutgoing.OrderReady;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class UpdateCustomerTest {
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
        order = new Order(id, Calendar.getInstance().getTime(), storeId, customerId, employeeId, new State(), articles);
        serviceMock = new ServiceMock();
    }

    @Test
    void customerValid() {
        order.setArticlesReady();
        new UpdateCustomer().modify(order, new CustomerResponse(order.getId(), true), serviceMock);
        assertEquals(new OrderReady(order.getId(), order.getStoreId()), serviceMock.orderReady);
        assertNull(serviceMock.orderCancelled);
    }

    @Test
    void customerValidArticlesNotYet() {
        new UpdateCustomer().modify(order, new CustomerResponse(order.getId(), true), serviceMock);
        assertNull(serviceMock.orderReady);
        assertNull(serviceMock.orderCancelled);
    }

    @Test
    void customerNotValid() {
        new UpdateCustomer().modify(order, new CustomerResponse(order.getId(), false), serviceMock);
        assertEquals(new OrderCancelled(order.getId(), order.getStoreId()), serviceMock.orderCancelled);
        assertNull(serviceMock.orderReady);
    }
}