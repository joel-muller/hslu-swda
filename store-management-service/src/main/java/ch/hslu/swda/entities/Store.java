package ch.hslu.swda.entities;

import ch.hslu.swda.business.DatabaseConnector;
import ch.hslu.swda.business.Modifiable;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.micro.Service;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity("store")
public class Store {
    @Id
    private UUID id;
    private List<StoreArticle> articleList;
    private List<UUID> openOrders;

    public Store(UUID id, List<StoreArticle> articleList, List<UUID> openOrders) {
        this.id = id;
        this.articleList = articleList;
        this.openOrders = openOrders;
    }

    /**
     * Default constructor for the Store class - required for Morphia to work properly..
     * Initializes the store with a unique identifier and sets the article list to null.
     */
    public Store() {
        this.id = UUID.randomUUID();
        this.articleList = null;
        this.openOrders = null;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public List<StoreArticle> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<StoreArticle> articleList) {
        this.articleList = articleList;
    }

    public List<UUID> getOpenOrders() {
        return openOrders;
    }

    public void setOpenOrders(List<UUID> openOrders) {
        this.openOrders = openOrders;
    }

    public void addOrder(UUID orderId) {
        if (openOrders == null) {
            openOrders = new ArrayList<>();
        }
        openOrders.add(orderId);
    }

    public void removeOrder(UUID orderId) {
        if (openOrders == null) {
            openOrders = new ArrayList<>();
            return;
        }
        openOrders.remove(orderId);
    }

    public StoreArticle getArticle(int id) {
        if (articleList == null) {
            return null;
        }
        for (StoreArticle article : this.articleList) {
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
        articles.add(new StoreArticle(18, 200, 2, 3));
        return new Store(id, articles, new ArrayList<>());
    }

    public void modify(Modifiable modifiable, IngoingMessage response, Service service, DatabaseConnector dataBase) {
        modifiable.modify(this, response, service, dataBase);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Store store)) return false;
        return Objects.equals(id, store.id) && Objects.equals(articleList, store.articleList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, articleList);
    }
}
