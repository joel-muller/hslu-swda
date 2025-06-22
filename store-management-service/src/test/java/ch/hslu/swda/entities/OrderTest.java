package ch.hslu.swda.entities;

import static org.junit.jupiter.api.Assertions.*;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class OrderTest {
    @Test
    void testConstructorAndGetters() {
        UUID orderId = UUID.randomUUID();
        List<OrderArticle> articles = List.of(new OrderArticle(1, 5, false));

        Order order = new Order(orderId, articles);

        assertEquals(orderId, order.getId());
        assertEquals(articles, order.getCopyOfArticleOrderedList());
    }

    @Test
    void testCreateFromOrderRequest() {
        UUID orderId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        Map<Integer, Integer> articlesMap = Map.of(1, 5, 2, 10); // Random order

        Order order = Order.createFromOrderRequest(orderId, articlesMap);

        assertEquals(orderId, order.getId());
        assertEquals(2, order.getCopyOfArticleOrderedList().size());

        // Extract the article IDs and counts
        List<OrderArticle> orderArticles = order.getCopyOfArticleOrderedList();
        assertTrue(orderArticles.stream().anyMatch(article -> article.getId() == 1 && article.getCount() == 5 && !article.isReady()));
        assertTrue(orderArticles.stream().anyMatch(article -> article.getId() == 2 && article.getCount() == 10 && !article.isReady()));
    }


    @Test
    void testIsReadyWhenAllArticlesAreReady() {
        List<OrderArticle> articles = List.of(
                new OrderArticle(1, 5, true),
                new OrderArticle(2, 10, true)
        );

        Order order = new Order(UUID.randomUUID(), articles);

        assertTrue(order.isReady());
    }

    @Test
    void testIsReadyWhenSomeArticlesAreNotReady() {
        List<OrderArticle> articles = List.of(
                new OrderArticle(1, 5, true),
                new OrderArticle(2, 10, false)
        );

        Order order = new Order(UUID.randomUUID(), articles);

        assertFalse(order.isReady());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID orderId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        List<OrderArticle> articles = List.of(new OrderArticle(1, 5, false));

        Order order1 = new Order(orderId, articles);
        Order order2 = new Order(orderId, articles);
        Order order3 = new Order(UUID.randomUUID(), articles);

        assertEquals(order1, order2);
        assertEquals(order1.hashCode(), order2.hashCode());
        assertNotEquals(order1, order3);
    }

    @Test
    void testToString() {
        UUID orderId = UUID.randomUUID();
        List<OrderArticle> articles = List.of(new OrderArticle(1, 5, false));

        Order order = new Order(orderId, articles);
        String expected = "Order{id=" + orderId +  ", articleOrderedList=" + articles + "}";
        assertEquals(expected, order.toString());
    }

    @Test
    void testNotEqualsDifferentType() {
        Order order = new Order(UUID.randomUUID(), List.of());
        assertNotEquals(order, "SomeString");
        assertNotEquals(order, null);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.simple().forClass(Order.class)
                .verify();
    }

    @Test
    void getCopy() {
        UUID id = UUID.randomUUID();
        OrderArticle article1 = new OrderArticle(1, 10, false);
        OrderArticle article2 = new OrderArticle(2, 15, false);
        OrderArticle article3 = new OrderArticle(3, 5, true);
        List<OrderArticle> articles = new ArrayList<>();
        articles.add(article1);
        articles.add(article2);
        articles.add(article3);
        Order order = new Order(id, articles);
        assertEquals(order, order.getCopy());
    }

    @Test
    void getCopyOfOrderList() {
        UUID id = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        OrderArticle article1 = new OrderArticle(1, 10, false);
        OrderArticle article2 = new OrderArticle(2, 15, false);
        OrderArticle article3 = new OrderArticle(3, 5, true);
        List<OrderArticle> articles = new ArrayList<>();
        articles.add(article1);
        articles.add(article2);
        articles.add(article3);
        Order order = new Order(id, articles);
        Order order2 = new Order(id2, articles);
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order2);
        List<Order> ordersCopy = Order.getCopyOfOrderList(orders);
        for (int i = 0; i < orders.size(); i++) {
            assertEquals(orders.get(i), ordersCopy.get(i));
        }
    }
}