package ch.hslu.swda.entities;

import ch.hslu.swda.messagesIngoing.OrderRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Order {
    private final UUID id;
    private final List<OrderArticle> articleOrderedList;

    public Order(UUID id, List<OrderArticle> articleOrderedList) {
        this.id = id;
        this.articleOrderedList = OrderArticle.getCopyOfArticleOrderedList(articleOrderedList);
    }

    public static Order createFromOrderRequest(OrderRequest request) {
        List<OrderArticle> articleOrdered = OrderArticle.createListArticle(request.articles());
        return new Order(request.orderId(), articleOrdered);
    }

    public UUID getId() {
        return id;
    }


    public List<OrderArticle> getCopyOfArticleOrderedList() {
        return OrderArticle.getCopyOfArticleOrderedList(articleOrderedList);
    }

    public Order getCopy() {
        return new Order(id, getCopyOfArticleOrderedList());
    }

    public static List<Order> getCopyOfOrderList(List<Order> list) {
        List<Order> newList = new ArrayList<>();
        for (Order order : list) {
            newList.add(order.getCopy());
        }
        return newList;
    }

    public boolean isReady() {
        for (OrderArticle article : articleOrderedList) {
            if (!article.isReady()) {
                return false;
            }
        }
        return true;
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

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", articleOrderedList=" + articleOrderedList +
                '}';
    }
}
