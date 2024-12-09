package ch.hslu.swda.persistence;

import ch.hslu.swda.entities.OrderArticle;
import ch.hslu.swda.messagesIngoing.OrderRequest;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.List;
import java.util.UUID;

@Entity("order")
public class DBOrder {
    @Id
    private UUID id;
    private List<DBOrderArticle> articleOrderedList;
    private boolean finished;

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

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}