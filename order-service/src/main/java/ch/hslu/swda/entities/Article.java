package ch.hslu.swda.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.*;

@Entity("articles")
public class Article {
    @Id
    private final UUID orderId;
    private final int articleId;
    private final int count;
    private boolean delivered;

    public Article(final int articleId, final int count) {
        this.orderId = UUID.randomUUID();
        this.articleId = articleId;
        this.count = count;
        this.delivered = false;
    }

    public static List<Article> createListArticle(Map<Integer, Integer> map) {
        ArrayList<Article> list = new ArrayList<>();
        for (Map.Entry<Integer, Integer> pair : map.entrySet()) {
            list.add(new Article(pair.getKey(), pair.getValue()));
        }
        return list;
    }

    public int getArticleId() {
        return articleId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public int getCount() {
        return count;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + articleId +
                ", count=" + count +
                ", delivered=" + delivered +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return Objects.equals(orderId, article.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }
}
