package ch.hslu.swda.persistence;

import dev.morphia.annotations.Entity;

import java.util.List;
import java.util.UUID;

@Entity("order")
public class DBOrder {
    private UUID id;
    private List<DBOrderArticle> articleOrderedList;

    public DBOrder(UUID id, List<DBOrderArticle> articleOrderedList) {
        this.id = id;
        this.articleOrderedList = articleOrderedList;
    }

    public DBOrder() {
        this.id = UUID.randomUUID();
        this.articleOrderedList = null;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }



    public List<DBOrderArticle> getArticleOrderedList() {
        return articleOrderedList;
    }

    public void setArticleOrderedList(List<DBOrderArticle> articleOrderedList) {
        this.articleOrderedList = articleOrderedList;
    }

}