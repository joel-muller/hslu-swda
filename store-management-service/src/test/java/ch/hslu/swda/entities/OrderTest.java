package ch.hslu.swda.entities;

import static org.junit.jupiter.api.Assertions.*;
import ch.hslu.swda.messagesIngoing.OrderRequest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

class OrderTest {
    @Test
    void testConstructorAndGetters() {
        UUID orderId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        List<OrderArticle> articles = List.of(new OrderArticle(1, 5, false));

        Order order = new Order(orderId, storeId, articles);

        assertEquals(orderId, order.getId());
        assertEquals(storeId, order.getStoreId());
        assertEquals(articles, order.getArticleOrderedList());
    }

    @Test
    void testCreateFromOrderRequest() {
        UUID orderId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        Map<Integer, Integer> articlesMap = Map.of(1, 5, 2, 10); // Random order

        // Create the OrderRequest instance directly
        OrderRequest request = new OrderRequest(orderId, employeeId, storeId, articlesMap);

        Order order = Order.createFromOrderRequest(request);

        assertEquals(orderId, order.getId());
        assertEquals(storeId, order.getStoreId());
        assertEquals(2, order.getArticleOrderedList().size());

        // Extract the article IDs and counts
        List<OrderArticle> orderArticles = order.getArticleOrderedList();
        assertTrue(orderArticles.stream().anyMatch(article -> article.getId() == 1 && article.getCount() == 5 && !article.isReady()));
        assertTrue(orderArticles.stream().anyMatch(article -> article.getId() == 2 && article.getCount() == 10 && !article.isReady()));
    }


    @Test
    void testIsReadyWhenAllArticlesAreReady() {
        List<OrderArticle> articles = List.of(
                new OrderArticle(1, 5, true),
                new OrderArticle(2, 10, true)
        );

        Order order = new Order(UUID.randomUUID(), UUID.randomUUID(), articles);

        assertTrue(order.isReady());
    }

    @Test
    void testIsReadyWhenSomeArticlesAreNotReady() {
        List<OrderArticle> articles = List.of(
                new OrderArticle(1, 5, true),
                new OrderArticle(2, 10, false)
        );

        Order order = new Order(UUID.randomUUID(), UUID.randomUUID(), articles);

        assertFalse(order.isReady());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID orderId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        List<OrderArticle> articles = List.of(new OrderArticle(1, 5, false));

        Order order1 = new Order(orderId, storeId, articles);
        Order order2 = new Order(orderId, storeId, articles);
        Order order3 = new Order(UUID.randomUUID(), storeId, articles);

        assertEquals(order1, order2);
        assertEquals(order1.hashCode(), order2.hashCode());
        assertNotEquals(order1, order3);
    }

    @Test
    void testToString() {
        UUID orderId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        List<OrderArticle> articles = List.of(new OrderArticle(1, 5, false));

        Order order = new Order(orderId, storeId, articles);
        String expected = "Order{id=" + orderId + ", storeId=" + storeId + ", articleOrderedList=" + articles + "}";
        assertEquals(expected, order.toString());
    }

    @Test
    void testNotEqualsDifferentType() {
        Order order = new Order(UUID.randomUUID(), UUID.randomUUID(), List.of());
        assertNotEquals(order, "SomeString");
        assertNotEquals(order, null);
    }
}