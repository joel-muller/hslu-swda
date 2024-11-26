package ch.hslu.swda.entities;

import ch.hslu.swda.messages.OrderRequest;
import ch.hslu.swda.messages.OrderUpdate;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity("order")
public class Order {
    @Id
    private UUID id;
    private UUID storeId;
    private List<ArticleOrdered> articleOrderedList;

    public Order(UUID id, UUID storeId, List<ArticleOrdered> articleOrderedList) {
        this.id = id;
        this.storeId = storeId;
        this.articleOrderedList = articleOrderedList;
    }

    public Order() {
        this.id = UUID.randomUUID();
        this.storeId = UUID.randomUUID();
        this.articleOrderedList = null;
    }

    public static Order createFromOrderRequest(OrderRequest request) {
        List<ArticleOrdered> articleOrdered = ArticleOrdered.createListArticle(request.articles());
        return new Order(request.orderId(), request.storeId(), articleOrdered);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public List<ArticleOrdered> getArticleOrderedList() {
        return articleOrderedList;
    }

    public void setArticleOrderedList(List<ArticleOrdered> articleOrderedList) {
        this.articleOrderedList = articleOrderedList;
    }

    public OrderUpdate generateOrderUpdate() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(id, order.id) && Objects.equals(storeId, order.storeId) && Objects.equals(articleOrderedList, order.articleOrderedList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, storeId, articleOrderedList);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", storeId=" + storeId +
                ", articleOrderedList=" + articleOrderedList +
                '}';
    }
}
