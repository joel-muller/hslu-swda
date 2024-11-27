package ch.hslu.swda.business;

import ch.hslu.swda.entities.Article;
import ch.hslu.swda.entities.Order;

import ch.hslu.swda.entities.State;
import ch.hslu.swda.messages.*;
import ch.hslu.swda.micro.Service;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ModifyValidityTest {
    @Test
    void testOrderValidOrderState() {
        UUID id = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        Map<Integer, Integer> articlesMap = new HashMap<Integer, Integer>();
        articlesMap.put(11, 2);
        articlesMap.put(33, 34);
        articlesMap.put(44, 6);
        articlesMap.put(5, 9);
        List<Article> articles = Article.createListArticle(articlesMap);
        Order order = new Order(id, articles, storeId, customerId, employeeId);

        ServiceMock service = new ServiceMock();
        VerifyResponse response = new VerifyResponse(id, true);
        order.modify(new ModifyValidity(), response, service);

        State state = new State();
        state.setValid(true);
        assertEquals(state, order.getState());
    }

    @Test
    void testOrderValidStoreRequest() {
        UUID id = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        Map<Integer, Integer> articlesMap = new HashMap<Integer, Integer>();
        articlesMap.put(11, 2);
        articlesMap.put(33, 34);
        articlesMap.put(44, 6);
        articlesMap.put(5, 9);
        List<Article> articles = Article.createListArticle(articlesMap);
        Order order = new Order(id, articles, storeId, customerId, employeeId);

        ServiceMock service = new ServiceMock();
        VerifyResponse response = new VerifyResponse(id, true);
        order.modify(new ModifyValidity(), response, service);
        assertEquals(new StoreRequest(id, articlesMap, employeeId, storeId), service.storeRequest);
    }

    @Test
    void testOrderValidCustomerRequest() {
        UUID id = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        Map<Integer, Integer> articlesMap = new HashMap<Integer, Integer>();
        articlesMap.put(11, 2);
        articlesMap.put(33, 34);
        articlesMap.put(44, 6);
        articlesMap.put(5, 9);
        List<Article> articles = Article.createListArticle(articlesMap);
        Order order = new Order(id, articles, storeId, customerId, employeeId);

        ServiceMock service = new ServiceMock();
        VerifyResponse response = new VerifyResponse(id, true);
        order.modify(new ModifyValidity(), response, service);
        assertEquals(new CustomerRequest(customerId, employeeId, id), service.customerRequest);
    }

    @Test
    void testOrderValidLogMessage() {
        UUID id = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        Map<Integer, Integer> articlesMap = new HashMap<Integer, Integer>();
        articlesMap.put(11, 2);
        articlesMap.put(33, 34);
        articlesMap.put(44, 6);
        articlesMap.put(5, 9);
        List<Article> articles = Article.createListArticle(articlesMap);
        Order order = new Order(id, articles, storeId, customerId, employeeId);

        ServiceMock service = new ServiceMock();
        VerifyResponse response = new VerifyResponse(id, true);
        order.modify(new ModifyValidity(), response, service);
        assertEquals(new LogMessage(id, employeeId, "order.validate", "Order Validated, order id: " + id.toString()), service.logMessage);
    }


    @Test
    void testOrderInvalidOrderState() {
        UUID id = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        Map<Integer, Integer> articlesMap = new HashMap<Integer, Integer>();
        articlesMap.put(11, 2);
        articlesMap.put(33, 34);
        articlesMap.put(44, 6);
        articlesMap.put(5, 9);
        List<Article> articles = Article.createListArticle(articlesMap);
        Order order = new Order(id, articles, storeId, customerId, employeeId);

        ServiceMock service = new ServiceMock();
        VerifyResponse response = new VerifyResponse(id, false);
        order.modify(new ModifyValidity(), response, service);

        State state = new State();
        state.setCancelled(true);
        assertEquals(state, order.getState());
    }


    @Test
    void testOrderInvalidStoreRequest() {
        UUID id = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        Map<Integer, Integer> articlesMap = new HashMap<Integer, Integer>();
        articlesMap.put(11, 2);
        articlesMap.put(33, 34);
        articlesMap.put(44, 6);
        articlesMap.put(5, 9);
        List<Article> articles = Article.createListArticle(articlesMap);
        Order order = new Order(id, articles, storeId, customerId, employeeId);

        ServiceMock service = new ServiceMock();
        VerifyResponse response = new VerifyResponse(id, false);
        order.modify(new ModifyValidity(), response, service);
        assertNull(service.storeRequest);
    }

    @Test
    void testOrderInvalidCustomerRequest() {
        UUID id = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        Map<Integer, Integer> articlesMap = new HashMap<Integer, Integer>();
        articlesMap.put(11, 2);
        articlesMap.put(33, 34);
        articlesMap.put(44, 6);
        articlesMap.put(5, 9);
        List<Article> articles = Article.createListArticle(articlesMap);
        Order order = new Order(id, articles, storeId, customerId, employeeId);

        ServiceMock service = new ServiceMock();
        VerifyResponse response = new VerifyResponse(id, false);
        order.modify(new ModifyValidity(), response, service);
        assertNull(service.customerRequest);
    }

    @Test
    void testOrderInvalidLogMessage() {
        UUID id = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        Map<Integer, Integer> articlesMap = new HashMap<Integer, Integer>();
        articlesMap.put(11, 2);
        articlesMap.put(33, 34);
        articlesMap.put(44, 6);
        articlesMap.put(5, 9);
        List<Article> articles = Article.createListArticle(articlesMap);
        Order order = new Order(id, articles, storeId, customerId, employeeId);

        ServiceMock service = new ServiceMock();
        VerifyResponse response = new VerifyResponse(id, false);
        order.modify(new ModifyValidity(), response, service);
        assertEquals(new LogMessage(id, employeeId, "order.validate", "Order not validated, order id: " + id.toString()), service.logMessage);
    }


}