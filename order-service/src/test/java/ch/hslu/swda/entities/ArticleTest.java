package ch.hslu.swda.entities;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {
    private Article article;
    private Article article2;

    @BeforeEach
    void setUp() {
        article = new Article(1, 10);
        article2 = new Article(2, 14, new Price(4, 10));
    }

    @Test
    void getId() {
        assertEquals(1, article.getId(), "getId should return the correct id value");
    }

    @Test
    void getCount() {
        assertEquals(10, article.getCount(), "getCount should return the correct count value");
    }

    @Test
    void getPrice() {
        assertEquals(new Price(4, 10), article2.getPrice());
    }

    @Test
    void setPrice() {
        article2.setPrice(15, 5);
        assertEquals(new Price(15, 5), article2.getPrice());
    }

    @Test
    void isDelivered() {
        assertFalse(article.isDelivered(), "Initially, delivered should be false");

        article.setDelivered(true);
        assertTrue(article.isDelivered(), "After setting delivered to true, isDelivered should return true");

        article.setDelivered(false);
        assertFalse(article.isDelivered(), "After setting delivered to false, isDelivered should return false");
    }

    @Test
    void setDelivered() {
        article.setDelivered(true);
        assertTrue(article.isDelivered(), "setDelivered(true) should make isDelivered return true");

        article.setDelivered(false);
        assertFalse(article.isDelivered(), "setDelivered(false) should make isDelivered return false");
    }

    @Test
    void createListArticle() {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(1, 5);
        map.put(2, 10);
        map.put(3, 15);

        List<Article> articles = Article.createListArticle(map);

        assertEquals(3, articles.size(), "The list should contain three articles");
        assertEquals(new Article(1, 5), articles.get(0), "The first article should have id 1 and count 5");
        assertEquals(new Article(2, 10), articles.get(1), "The second article should have id 2 and count 10");
        assertEquals(new Article(3, 15), articles.get(2), "The third article should have id 3 and count 15");
    }

    @Test
    void testToString() {
        String expectedString = "Article{id=1, count=10, delivered=false, price=Price{0.00 Francs}}";
        assertEquals(expectedString, article.toString(), "toString should match the expected format for the article");

        article.setDelivered(true);
        String updatedString = "Article{id=1, count=10, delivered=true, price=Price{0.00 Francs}}";
        assertEquals(updatedString, article.toString(), "toString should reflect the current delivered status");
    }

    @Test
    void testEquals() {
        Article sameArticle = new Article(1, 10);
        Article differentArticle = new Article(2, 10);

        assertEquals(article, sameArticle, "Two articles with the same id and count should be equal");
        assertNotEquals(article, differentArticle, "Two articles with different ids should not be equal");

        article.setDelivered(true);
        assertNotEquals(article, sameArticle, "Articles with different delivered statuses should not be equal");
    }

    @Test
    void testHashCode() {
        Article sameArticle = new Article(1, 10);
        assertEquals(article.hashCode(), sameArticle.hashCode(), "Articles with the same fields should have the same hash code");

        article.setDelivered(true);
        assertNotEquals(article.hashCode(), sameArticle.hashCode(), "Articles with different fields should have different hash codes");
    }

    @Test
    void testEqualsAndHashCode() {
        EqualsVerifier.simple().forClass(Article.class)
                .verify();
    }

}