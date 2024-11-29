package ch.hslu.swda.entities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class OrderArticleTest {
    @Test
    void testConstructorAndGetters() {
        OrderArticle article = new OrderArticle(1, 10, false);
        assertEquals(1, article.getId());
        assertEquals(10, article.getCount());
        assertFalse(article.isReady());
    }

    @Test
    void testSetReady() {
        OrderArticle article = new OrderArticle(1, 10, false);
        assertFalse(article.isReady());
        article.setReady(true);
        assertTrue(article.isReady());
    }

    @Test
    void testCreateListArticle() {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(1, 5);
        map.put(2, 10);

        List<OrderArticle> articles = OrderArticle.createListArticle(map);
        assertEquals(2, articles.size());

        OrderArticle firstArticle = articles.get(0);
        assertEquals(1, firstArticle.getId());
        assertEquals(5, firstArticle.getCount());
        assertFalse(firstArticle.isReady());

        OrderArticle secondArticle = articles.get(1);
        assertEquals(2, secondArticle.getId());
        assertEquals(10, secondArticle.getCount());
        assertFalse(secondArticle.isReady());
    }

    @Test
    void testEqualsAndHashCode() {
        OrderArticle article1 = new OrderArticle(1, 10, false);
        OrderArticle article2 = new OrderArticle(1, 10, false);
        OrderArticle article3 = new OrderArticle(2, 5, true);

        assertEquals(article1, article2);
        assertEquals(article1.hashCode(), article2.hashCode());
        assertNotEquals(article1, article3);
        assertNotEquals(article1.hashCode(), article3.hashCode());
    }

    @Test
    void testToString() {
        OrderArticle article = new OrderArticle(1, 10, true);
        String expected = "OrderArticle{id=1, count=10, ready=true}";
        assertEquals(expected, article.toString());
    }

    @Test
    void testNotEqualsDifferentType() {
        OrderArticle article = new OrderArticle(1, 10, false);
        assertNotEquals(article, "SomeString");
        assertNotEquals(article, null);
    }

    @Test
    void testReadyStateChange() {
        OrderArticle article = new OrderArticle(1, 10, false);
        assertFalse(article.isReady());
        article.setReady(true);
        assertTrue(article.isReady());
    }
}