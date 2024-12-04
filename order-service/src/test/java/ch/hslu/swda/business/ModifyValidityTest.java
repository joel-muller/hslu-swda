package ch.hslu.swda.business;

import ch.hslu.swda.entities.Article;
import ch.hslu.swda.entities.Order;

import ch.hslu.swda.entities.State;
import ch.hslu.swda.messagesIngoing.*;
import ch.hslu.swda.messagesOutgoing.CustomerRequest;
import ch.hslu.swda.messagesOutgoing.LogMessage;
import ch.hslu.swda.messagesOutgoing.StoreRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ModifyValidityTest {
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
        VerifyResponse response = new VerifyResponse(id, true, francsResponse, centimesResponse);
        new ModifyValidity().modify(order, response, serviceMock);
        State state = new State();
        state.setValid(true);
        assertEquals(state, order.getCopyOfState());
    }

    @Test
    void testOrderValidStoreRequest() {
        VerifyResponse response = new VerifyResponse(id, true, francsResponse, centimesResponse);
        new ModifyValidity().modify(order, response, serviceMock);
        assertEquals(new StoreRequest(id, articlesMap, employeeId, storeId), serviceMock.storeRequest);
    }

    @Test
    void testOrderValidCustomerRequest() {
        VerifyResponse response = new VerifyResponse(id, true, francsResponse, centimesResponse);
        new ModifyValidity().modify(order, response, serviceMock);
        assertEquals(new CustomerRequest(customerId, employeeId, id), serviceMock.customerRequest);
    }

    @Test
    void testOrderValidLogMessage() {
        VerifyResponse response = new VerifyResponse(id, true, francsResponse, centimesResponse);
        new ModifyValidity().modify(order, response, serviceMock);
        assertEquals(new LogMessage(id, employeeId, "order.validate", "Order Validated, order id: " + id.toString()), serviceMock.logMessage);
    }

    @Test
    void testOrderInvalidOrderState() {
        VerifyResponse response = new VerifyResponse(id, false, francsResponse, centimesResponse);
        new ModifyValidity().modify(order, response, serviceMock);
        State state = new State();
        state.setCancelled(true);
        assertEquals(state, order.getCopyOfState());
    }


    @Test
    void testOrderInvalidStoreRequest() {
        VerifyResponse response = new VerifyResponse(id, false, new HashMap<>(), new HashMap<>());
        new ModifyValidity().modify(order, response, serviceMock);
        assertNull(serviceMock.storeRequest);
    }

    @Test
    void testOrderInvalidCustomerRequest() {
        VerifyResponse response = new VerifyResponse(id, false, new HashMap<>(), new HashMap<>());
        new ModifyValidity().modify(order, response, serviceMock);
        assertNull(serviceMock.customerRequest);
    }

    @Test
    void testOrderInvalidLogMessage() {
        VerifyResponse response = new VerifyResponse(id, false, new HashMap<>(), new HashMap<>());
        new ModifyValidity().modify(order, response, serviceMock);
        assertEquals(new LogMessage(id, employeeId, "order.validate", "Order not validated, order id: " + id.toString()), serviceMock.logMessage);
    }


}