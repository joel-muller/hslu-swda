package ch.hslu.swda.entities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ArticleOrderedTest {
    @Test
    void testConstructorAndGetters() {
        ArticleOrdered article = new ArticleOrdered(1, 10);

        assertEquals(1, article.getId(), "ID should match the value set in the constructor.");
        assertEquals(10, article.getCount(), "Count should match the value set in the constructor.");
        assertFalse(article.isReady(), "New articles should be not ready by default.");
    }

    @Test
    void testSetReady() {
        ArticleOrdered article = new ArticleOrdered(1, 10);

        article.setReady(true);
        assertTrue(article.isReady(), "Article should be marked as ready after calling setReady(true).");

        article.setReady(false);
        assertFalse(article.isReady(), "Article should be marked as not ready after calling setReady(false).");
    }

    @Test
    void testCreateListArticle() {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(1, 10);
        map.put(2, 20);
        map.put(3, 30);

        List<ArticleOrdered> articles = ArticleOrdered.createListArticle(map);

        assertEquals(3, articles.size(), "The list size should match the number of entries in the map.");
        assertTrue(articles.contains(new ArticleOrdered(1, 10)), "First article should match the first map entry.");
        assertTrue(articles.contains(new ArticleOrdered(2, 20)), "First article should match the first map entry.");
        assertTrue(articles.contains(new ArticleOrdered(3, 30)), "First article should match the first map entry.");
    }

    @Test
    void testToString() {
        ArticleOrdered article = new ArticleOrdered(1, 10);
        String expected = "Article{id=1, count=10}";

        assertEquals(expected, article.toString(), "toString() output should match the expected format.");
    }

    @Test
    void testEqualsAndHashCode() {
        ArticleOrdered article1 = new ArticleOrdered(1, 10);
        ArticleOrdered article2 = new ArticleOrdered(1, 10);
        ArticleOrdered article3 = new ArticleOrdered(2, 20);

        assertEquals(article1, article2, "Articles with the same ID and count should be equal.");
        assertNotEquals(article1, article3, "Articles with different ID or count should not be equal.");
        assertEquals(article1.hashCode(), article2.hashCode(), "Equal articles should have the same hashCode.");
        assertNotEquals(article1.hashCode(), article3.hashCode(), "Different articles should have different hashCodes.");
    }
}