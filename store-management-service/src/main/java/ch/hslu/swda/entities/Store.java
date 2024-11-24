package ch.hslu.swda.entities;

import ch.hslu.swda.business.StoreModifiable;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity("store")
public class Store {
    @Id
    private UUID id;
    private List<StoreArticle> articleList;

    public Store(UUID id, List<StoreArticle> articleList) {
        this.id = id;
        this.articleList = articleList;
    }


    /**
     * Default constructor for the Store class - required for Morphia to work properly..
     * Initializes the store with a unique identifier and sets the article list to null.
     */
    public Store() {
        this.id = UUID.randomUUID();
        this.articleList = null;
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

    public void modify(StoreModifiable modifiable) {
        modifiable.modify(this);
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
