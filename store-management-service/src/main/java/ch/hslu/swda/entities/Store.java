package ch.hslu.swda.entities;

import ch.hslu.swda.business.Modifiable;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.micro.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Store {
    final private UUID id;
    private List<StoreArticle> articleList;
    private List<Order> openOrders;

    public Store(final UUID id, List<StoreArticle> articleList, List<Order> openOrders) {
        this.id = id;
        this.articleList = articleList;
        this.openOrders = openOrders;
    }

    public List<StoreArticle> getArticleList() {
        return articleList;
    }

    public List<Order> getOpenOrders() {
        return openOrders;
    }

    public void addOrder(Order order) {
        openOrders.add(order);
    }

    public void removeOrder(UUID orderId) {
        openOrders.removeIf(order -> Objects.equals(order.getId(), orderId));
    }

    public StoreArticle getArticle(int id) {
        for (StoreArticle article : articleList) {
            if (article.getId() == id) {
                return article;
            }
        }
        return null;
    }

    public static Store createExampleStore(UUID id) {
        List<StoreArticle> articles = new ArrayList<StoreArticle>();
        articles.add(new StoreArticle(14, 200, 5, 5));
        articles.add(new StoreArticle(12, 200, 3, 3));
        //articles.add(new StoreArticle(18, 200, 2, 3));
        return new Store(id, articles, new ArrayList<>());
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Store store)) return false;
        return Objects.equals(id, store.id) && Objects.equals(articleList, store.articleList) && Objects.equals(openOrders, store.openOrders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, articleList, openOrders);
    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", articleList=" + articleList +
                ", openOrders=" + openOrders +
                '}';
    }
}
