package ch.hslu.swda.entities;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {
    @Test
    void createListArticle() {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(1, 4);
        map.put(234, 4423);
        List<Article> list = Article.createListArticle(map);
        assertEquals(1, list.getFirst().getArticleId());
        assertEquals(234, list.get(1).getArticleId());
    }

    @Test
    void getArticleId() {
        int articleId = 6544;
        int count = 556651;
        Article article = new Article(articleId, count);
        assertEquals(articleId, article.getArticleId());
    }

    @Test
    void getCount() {
        int articleId = 1339;
        int count = 97556;
        Article article = new Article(articleId, count);
        assertEquals(count, article.getCount());
    }

    @Test
    void isDeliveredAndSetDelivered() {
        int articleId = 69;
        int count = 444;
        Article article = new Article(articleId, count);
        assertFalse(article.isDelivered());
        article.setDelivered(true);
        assertTrue(article.isDelivered());
    }

    @Test
    void testToString() {
        int articleId = 12;
        int count = 333;
        Article article = new Article(articleId, count);
        assertEquals("Article{id=12, count=333, delivered=false}", article.toString());
    }

}