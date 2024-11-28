package ch.hslu.swda.persistence;

import ch.hslu.swda.business.Modifiable;
import ch.hslu.swda.entities.StoreArticle;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.micro.Service;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity("store")
public class DBStore {
    @Id
    private UUID id;
    private List<DBStoreArticle> articleList;
    private List<DBOrder> openOrders;

    public DBStore(UUID id, List<DBStoreArticle> articleList, List<DBOrder> openOrders) {
        this.id = id;
        this.articleList = articleList;
        this.openOrders = openOrders;
    }

    /**
     * Default constructor for the Store class - required for Morphia to work properly..
     * Initializes the store with a unique identifier and sets the article list to null.
     */
    public DBStore() {
        this.id = UUID.randomUUID();
        this.articleList = null;
        this.openOrders = null;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<DBStoreArticle> getArticleList() {
        if (articleList == null) {
            return new ArrayList<>();
        }
        return articleList;
    }

    public void setArticleList(List<DBStoreArticle> articleList) {
        this.articleList = articleList;
    }

    public List<DBOrder> getOpenOrders() {
        if (openOrders == null) {
            return new ArrayList<>();
        }
        return openOrders;
    }

    public void setOpenOrders(List<DBOrder> openOrders) {
        this.openOrders = openOrders;
    }
}