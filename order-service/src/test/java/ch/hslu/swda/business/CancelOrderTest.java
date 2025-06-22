package ch.hslu.swda.business;

import ch.hslu.swda.entities.Article;
import ch.hslu.swda.entities.Order;
import ch.hslu.swda.entities.StateEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CancelOrderTest {
    Order order;
    UUID id = UUID.randomUUID();
    UUID storeId = UUID.randomUUID();
    UUID customerId = UUID.randomUUID();
    UUID employeeId = UUID.randomUUID();
    Map<Integer, Integer> articlesMap;
    ServiceMock serviceMock;
    Map<Integer, Integer> francsResponse;
    Map<Integer, Integer> centimesResponse;
    List<Article> articles;

    @BeforeEach
    void setup() {
        articlesMap = new HashMap<Integer, Integer>();
        articlesMap.put(11, 2);
        articlesMap.put(33, 34);
        articlesMap.put(44, 6);
        articlesMap.put(5, 9);
        articles = Article.createListArticle(articlesMap);
        serviceMock = new ServiceMock();
    }

    @Test
    void cancelOrderNotReady() {
        order = new Order(id, Calendar.getInstance().getTime(), storeId, customerId, employeeId, articles, StateEnum.ARTICLE_VALIDATED, false);
        new CancelOrder().modify(order, null, serviceMock);
        assertTrue(order.isCancelled());
        assertEquals(order.getOrderCancelled(), serviceMock.storeOrderCancelled);
    }

    @Test
    void cancelOrderReady() {
        order = new Order(id, Calendar.getInstance().getTime(), storeId, customerId, employeeId, articles, StateEnum.READY, false);
        new CancelOrder().modify(order, null, serviceMock);
        assertFalse(order.isCancelled());
        assertNull(serviceMock.storeOrderCancelled);
    }
}