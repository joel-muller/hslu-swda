package ch.hslu.swda.entities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class StoreArticleTest {
    @Test
    void testConstructorAndGetters() {
        StoreArticle article = new StoreArticle(1, 50, 10, 5);

        assertEquals(1, article.getId());
        assertEquals(50, article.getActualQuantity());
        assertEquals(10, article.getMinimumQuantity());
        assertEquals(5, article.getRefillCount());
    }

    @Test
    void testDefaultConstructor() {
        StoreArticle article = new StoreArticle();

        assertEquals(0, article.getId());
        assertEquals(0, article.getActualQuantity());
        assertEquals(0, article.getMinimumQuantity());
        assertEquals(0, article.getRefillCount());
    }

    @Test
    void testCreateArticleList() {
        Map<Integer, Integer> articlesMap = Map.of(1, 50, 2, 100);
        List<StoreArticle> articleList = StoreArticle.createArticleList(articlesMap);

        assertEquals(2, articleList.size());

        // Convert the articleList to a set of article IDs and quantities for comparison
        List<Integer> expectedIds = List.of(1, 2);
        List<Integer> expectedQuantities = List.of(50, 100);

        // Extract the ids and quantities from the articleList
        List<Integer> actualIds = new ArrayList<>();
        List<Integer> actualQuantities = new ArrayList<>();

        for (StoreArticle article : articleList) {
            actualIds.add(article.getId());
            actualQuantities.add(article.getActualQuantity());
        }

        // Assert that both expected and actual ids and quantities match
        assertTrue(actualIds.containsAll(expectedIds));
        assertTrue(actualQuantities.containsAll(expectedQuantities));
    }


    @Test
    void testGetWithRefillBackWhenNotEnoughQuantity() {
        StoreArticle article = new StoreArticle(1, 5, 3, 10);

        int result = article.getWithRefillBack(10); // Try to get more than available
        assertEquals(-1, result); // Not enough quantity, should return -1
    }

    @Test
    void testGetWithRefillBackWhenEnoughQuantityButNeedsRefill() {
        StoreArticle article = new StoreArticle(1, 5, 3, 10);

        int result = article.getWithRefillBack(3); // Take 3, leaving 2
        assertEquals(10, result); // Refill is needed, should return the refill count
        assertEquals(2, article.getActualQuantity()); // Quantity should decrease by 3
    }

    @Test
    void testGetWithRefillBackWhenEnoughQuantity() {
        StoreArticle article = new StoreArticle(1, 5, 3, 10);

        int result = article.getWithRefillBack(2); // Take 2, leaving 3
        assertEquals(0, result); // No refill needed, should return 0
        assertEquals(3, article.getActualQuantity()); // Quantity should decrease by 2
    }

    @Test
    void testDecrementQuantity() {
        StoreArticle article = new StoreArticle(1, 50, 10, 5);

        article.decrementQuantity(10);
        assertEquals(40, article.getActualQuantity());
    }

    @Test
    void testEqualsAndHashCode() {
        StoreArticle article1 = new StoreArticle(1, 50, 10, 5);
        StoreArticle article2 = new StoreArticle(1, 50, 10, 5);
        StoreArticle article3 = new StoreArticle(2, 100, 20, 10);

        assertEquals(article1, article2); // Same ID, quantity, etc.
        assertNotEquals(article1, article3); // Different ID
        assertNotEquals(article1, null); // Not equal to null
        assertNotEquals(article1, "SomeString"); // Not equal to different type

        assertEquals(article1.hashCode(), article2.hashCode()); // Same hash code for equal objects
        assertNotEquals(article1.hashCode(), article3.hashCode()); // Different hash code for different objects
    }

    @Test
    void testToString() {
        StoreArticle article = new StoreArticle(1, 50, 10, 5);
        String expectedString = "StoreArticle{id=1, actualQuantity=50, minimumQuantity=10, refillCount=5}";
        assertEquals(expectedString, article.toString());
    }
}