package ch.hslu.swda.business;

import ch.hslu.swda.entities.Article;
import ch.hslu.swda.entities.Order;

import ch.hslu.swda.entities.StateEnum;
import ch.hslu.swda.messagesIngoing.*;
import ch.hslu.swda.messagesOutgoing.CustomerValidate;
import ch.hslu.swda.messagesOutgoing.LogMessage;
import ch.hslu.swda.messagesOutgoing.StoreRequestArticles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ReceiveValidityTest {
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
        order = new Order(id, Calendar.getInstance().getTime(), storeId, customerId, employeeId, articles, StateEnum.STORED, false);

        serviceMock = new ServiceMock();

        francsResponse = new HashMap<>();
        centimesResponse = new HashMap<>();
        francsResponse.put(11, 15);
        francsResponse.put(33, 14);
        francsResponse.put(44, 5);
        francsResponse.put(5, 5);
        centimesResponse.put(11, 0);
        centimesResponse.put(33, 0);
        centimesResponse.put(44, 0);
        centimesResponse.put(5, 0);
    }

    @Test
    void testOrderValidState() {
        OrderReceiveValidity response = new OrderReceiveValidity(id, true, francsResponse, centimesResponse);
        new ReceiveValidity().modify(order, response, serviceMock);
        assertEquals(StateEnum.ARTICLE_VALIDATED, order.getState());
    }

    @Test
    void testOrderValidStoreRequest() {
        OrderReceiveValidity response = new OrderReceiveValidity(id, true, francsResponse, centimesResponse);
        new ReceiveValidity().modify(order, response, serviceMock);
        assertEquals(new StoreRequestArticles(id, articlesMap, employeeId, storeId), serviceMock.storeRequestArticles);
    }

    @Test
    void testOrderValidCustomerRequest() {
        OrderReceiveValidity response = new OrderReceiveValidity(id, true, francsResponse, centimesResponse);
        new ReceiveValidity().modify(order, response, serviceMock);
        assertEquals(new CustomerValidate(customerId, employeeId, id), serviceMock.customerValidate);
    }

    @Test
    void testOrderValidLogMessage() {
        OrderReceiveValidity response = new OrderReceiveValidity(id, true, francsResponse, centimesResponse);
        new ReceiveValidity().modify(order, response, serviceMock);
        assertEquals(new LogMessage(id, employeeId, "order.validate", "Order Validated, order id: " + id.toString()), serviceMock.logMessage);
    }

    @Test
    void testOrderInvalidOrderState() {
        OrderReceiveValidity response = new OrderReceiveValidity(id, false, francsResponse, centimesResponse);
        new ReceiveValidity().modify(order, response, serviceMock);
        assertTrue(order.isCancelled());
    }


    @Test
    void testOrderInvalidStoreRequest() {
        OrderReceiveValidity response = new OrderReceiveValidity(id, false, new HashMap<>(), new HashMap<>());
        new ReceiveValidity().modify(order, response, serviceMock);
        assertNull(serviceMock.storeRequestArticles);
    }

    @Test
    void testOrderInvalidCustomerRequest() {
        OrderReceiveValidity response = new OrderReceiveValidity(id, false, new HashMap<>(), new HashMap<>());
        new ReceiveValidity().modify(order, response, serviceMock);
        assertNull(serviceMock.customerValidate);
    }

    @Test
    void testOrderInvalidLogMessage() {
        OrderReceiveValidity response = new OrderReceiveValidity(id, false, new HashMap<>(), new HashMap<>());
        new ReceiveValidity().modify(order, response, serviceMock);
        assertEquals(new LogMessage(id, employeeId, "order.validate", "Order not validated, order id: " + id.toString()), serviceMock.logMessage);
    }


}