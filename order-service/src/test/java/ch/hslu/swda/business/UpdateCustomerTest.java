package ch.hslu.swda.business;

import ch.hslu.swda.entities.Article;
import ch.hslu.swda.entities.Order;
import ch.hslu.swda.entities.StateEnum;
import ch.hslu.swda.messagesIngoing.OrderCustomerValidity;
import ch.hslu.swda.messagesOutgoing.StoreOrderCancelled;
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
        order = new Order(id, Calendar.getInstance().getTime(), storeId, customerId, employeeId, articles, StateEnum.ARTICLE_VALIDATED, false);
        serviceMock = new ServiceMock();
    }

    @Test
    void customerValid() {
        order.setArticleInStore(11);
        order.setArticleInStore(33);
        order.setArticleInStore(44);
        order.setArticleInStore(5);
        new UpdateCustomer().modify(order, new OrderCustomerValidity(order.getId(), true), serviceMock);
        assertEquals(order.getOrderReady(), serviceMock.storeOrderReady);
        assertEquals(order.getInvoice(), serviceMock.invoiceCreate);
        assertNull(serviceMock.storeOrderCancelled);
    }

    @Test
    void customerValidArticlesNotYet() {
        new UpdateCustomer().modify(order, new OrderCustomerValidity(order.getId(), true), serviceMock);
        assertNull(serviceMock.storeOrderReady);
        assertNull(serviceMock.storeOrderCancelled);
        assertNull(serviceMock.invoiceCreate);
    }

    @Test
    void customerNotValid() {
        new UpdateCustomer().modify(order, new OrderCustomerValidity(order.getId(), false), serviceMock);
        assertEquals(new StoreOrderCancelled(order.getId(), order.getStoreId()), serviceMock.storeOrderCancelled);
        assertNull(serviceMock.storeOrderReady);
        assertNull(serviceMock.invoiceCreate);
    }
}