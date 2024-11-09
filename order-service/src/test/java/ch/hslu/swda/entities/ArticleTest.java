package ch.hslu.swda.entities;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {
    @Test
    void createListArticle() {
        UUID id = UUID.randomUUID();
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(1, 4);
        map.put(234, 4423);
        List<Article> list = Article.createListArticle(id, map);
        assertEquals(1, list.getFirst().getId());
        assertEquals(234, list.get(1).getId());
    }

    @Test
    void getArticleId() {
        UUID id = UUID.randomUUID();
        int articleId = 6544;
        int count = 556651;
        Article article = new Article(id, articleId, count);
        assertEquals(articleId, article.getId());
    }

    @Test
    void getCount() {
        UUID id = UUID.randomUUID();
        int articleId = 1339;
        int count = 97556;
        Article article = new Article(id, articleId, count);
        assertEquals(count, article.getCount());
    }

    @Test
    void isDeliveredAndSetDelivered() {
        UUID id = UUID.randomUUID();
        int articleId = 69;
        int count = 444;
        Article article = new Article(id, articleId, count);
        assertFalse(article.isDelivered());
        article.setDelivered(true);
        assertTrue(article.isDelivered());
    }

    @Test
    void testToString() {
        UUID id = UUID.randomUUID();
        int articleId = 12;
        int count = 333;
        Article article = new Article(id, articleId, count);
        assertEquals("Article{id=12, count=333, delivered=false}", article.toString());
    }

}