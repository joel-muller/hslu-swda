package ch.hslu.swda.entities;

import ch.hslu.swda.business.Modifiable;
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

    public StoreArticle getArticle(int id) {
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
        return new Store(id, articles);
    }

    public void modify(Modifiable modifiable) {
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
