package ch.hslu.swda.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity("store")
public class Store {
    @Id
    private final UUID id;
    private List<ArticleStore> articleList;

    public Store(UUID id, List<ArticleStore> articleList) {
        this.id = id;
        this.articleList = articleList;
    }

    public UUID getId() {
        return id;
    }

    public List<ArticleStore> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<ArticleStore> articleList) {
        this.articleList = articleList;
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
