package ch.hslu.swda.entities;

import ch.hslu.swda.messagesIngoing.OrderRequest;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Order {
    private final UUID id;
    private final UUID storeId;
    private List<OrderArticle> articleOrderedList;

    public Order(UUID id, UUID storeId, List<OrderArticle> articleOrderedList) {
        this.id = id;
        this.storeId = storeId;
        this.articleOrderedList = articleOrderedList;
    }

    public static Order createFromOrderRequest(OrderRequest request) {
        List<OrderArticle> articleOrdered = OrderArticle.createListArticle(request.articles());
        return new Order(request.orderId(), request.storeId(), articleOrdered);
    }


    public UUID getId() {
        return id;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public List<OrderArticle> getArticleOrderedList() {
        return articleOrderedList;
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
