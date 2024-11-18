package ch.hslu.swda.entities;

import ch.hslu.swda.messages.OrderRequest;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity("order")
public class Order {
    @Id
    private final UUID id;
    private List<ArticleOrdered> articleOrderedList;

    public Order(UUID id, List<ArticleOrdered> articleOrderedList) {
        this.id = id;
        this.articleOrderedList = articleOrderedList;
    }

    public static Order createFromOrderRequest(OrderRequest request) {
        List<ArticleOrdered> articleOrdered = ArticleOrdered.createListArticle(request.articles());
        return new Order(request.orderId(), articleOrdered);
    }

    public UUID getId() {
        return id;
    }

    public List<ArticleOrdered> getArticleOrderedList() {
        return articleOrderedList;
    }

    public void setArticleOrderedList(List<ArticleOrdered> articleOrderedList) {
        this.articleOrderedList = articleOrderedList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(id, order.id) && Objects.equals(articleOrderedList, order.articleOrderedList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, articleOrderedList);
    }
}
