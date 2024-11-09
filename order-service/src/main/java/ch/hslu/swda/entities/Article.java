package ch.hslu.swda.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.util.*;

@Entity("article")
public class Article {
//    @Id
//    private final UUID articleId;
//    private final UUID orderId;
    private final int id;
    private final int count;
    private boolean delivered;

    public Article(final UUID orderId, final int id, final int count) {
//        this.orderId = orderId;
//        this.articleId = UUID.randomUUID();
        this.id = id;
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

    public int getId() {
        return id;
    }

//    public UUID getOrderId() {
//        return orderId;
//    }

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
                "id=" + id +
                ", count=" + count +
                ", delivered=" + delivered +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, count, delivered);
    }
}
