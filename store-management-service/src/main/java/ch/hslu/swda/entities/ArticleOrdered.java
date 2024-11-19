package ch.hslu.swda.entities;

import dev.morphia.annotations.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity("articleOrdered")
public class ArticleOrdered {
    private int id;
    private int count;
    private boolean ready;

    public ArticleOrdered(final int id, final int count) {
        this.id = id;
        this.count = count;
        this.ready = false;
    }

    public ArticleOrdered() {
        this.id = -1;
        this.count = -1;
        this.ready = false;
    }

    public static List<ArticleOrdered> createListArticle(Map<Integer, Integer> map) {
        ArrayList<ArticleOrdered> list = new ArrayList<>();
        for (Map.Entry<Integer, Integer> pair : map.entrySet()) {
            list.add(new ArticleOrdered(pair.getKey(), pair.getValue()));
        }
        return list;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", count=" + count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleOrdered article)) return false;
        return id == article.id && count == article.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, count);
    }
}
