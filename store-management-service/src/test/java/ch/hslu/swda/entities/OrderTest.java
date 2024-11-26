package ch.hslu.swda.entities;

import static org.junit.jupiter.api.Assertions.*;

import ch.hslu.swda.messages.OrderRequest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

class OrderTest {
    @Test
    void testConstructorAndGetters() {
        UUID orderId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        List<ArticleOrdered> articles = List.of(new ArticleOrdered(1, 10), new ArticleOrdered(2, 20));
        Order order = new Order(orderId, storeId, articles);

        assertEquals(orderId, order.getId(), "Order ID should match the value set in the constructor.");
        assertEquals(articles, order.getArticleOrderedList(), "Articles list should match the value set in the constructor.");
    }

    @Test
    void testSetArticleOrderedList() {
        UUID orderId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        List<ArticleOrdered> initialArticles = List.of(new ArticleOrdered(1, 10));
        List<ArticleOrdered> newArticles = List.of(new ArticleOrdered(2, 20), new ArticleOrdered(3, 30));

        Order order = new Order(orderId, storeId, initialArticles);
        order.setArticleOrderedList(newArticles);

        assertEquals(newArticles, order.getArticleOrderedList(), "The updated articles list should match the new value set.");
    }

    @Test
    void testCreateFromOrderRequest() {
        UUID orderId = UUID.randomUUID();
        Map<Integer, Integer> articleMap = Map.of(1, 10, 2, 20);
        OrderRequest request = new OrderRequest(orderId, orderId, orderId, articleMap);

        Order order = Order.createFromOrderRequest(request);

        assertEquals(orderId, order.getId(), "Order ID should match the ID in the OrderRequest.");
        assertEquals(2, order.getArticleOrderedList().size(), "Article list size should match the size of the map in the OrderRequest.");
        assertTrue(order.getArticleOrderedList().contains(new ArticleOrdered(1, 10)), "First article should match the first entry in the map.");
        assertTrue(order.getArticleOrderedList().contains(new ArticleOrdered(2, 20)), "First article should match the first entry in the map.");
    }

    @Test
    void testEqualsAndHashCode() {
        UUID orderId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        List<ArticleOrdered> articles = List.of(new ArticleOrdered(1, 10), new ArticleOrdered(2, 20));

        Order order1 = new Order(orderId, storeId, articles);
        Order order2 = new Order(orderId, storeId, articles);
        Order order3 = new Order(UUID.randomUUID(), storeId, articles);

        assertEquals(order1, order2, "Orders with the same ID and articles list should be equal.");
        assertNotEquals(order1, order3, "Orders with different IDs should not be equal.");
        assertEquals(order1.hashCode(), order2.hashCode(), "Equal orders should have the same hash code.");
        assertNotEquals(order1.hashCode(), order3.hashCode(), "Different orders should have different hash codes.");
    }
}