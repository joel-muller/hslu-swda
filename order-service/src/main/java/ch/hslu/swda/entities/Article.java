package ch.hslu.swda.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity("articles")
public class Article {
    @Id
    private final UUID orderId;
    private final int articleId;
    private final int count;
    private boolean delivered;

    public Article(final UUID orderId, final int articleId, final int count) {
        this.orderId = orderId;
        this.articleId = articleId;
        this.count = count;
        this.delivered = false;
    }

    public static List<Article> createListArticle(UUID orderId, Map<Integer, Integer> map) {
        ArrayList<Article> list = new ArrayList<>();
        for (Map.Entry<Integer, Integer> pair : map.entrySet()) {
            list.add(new Article(orderId, pair.getKey(), pair.getValue()));
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
}
