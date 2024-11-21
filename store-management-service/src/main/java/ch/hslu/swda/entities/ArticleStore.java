package ch.hslu.swda.entities;

import dev.morphia.annotations.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents an article in the context of a store.
 * This class is annotated with @Entity to indicate that it is a JPA entity.
 * It contains information about the article's ID and count.
 */
@Entity("articleStore")
public class ArticleStore {
    private int id;
    private int count;

    public ArticleStore(final int id, final int count) {
        this.id = id;
        this.count = count;
    }

    public ArticleStore() {
        this.id = -1;
        this.count = -1;
    }

    public static List<ArticleStore> createListArticle(Map<Integer, Integer> map) {
        ArrayList<ArticleStore> list = new ArrayList<>();
        for (Map.Entry<Integer, Integer> pair : map.entrySet()) {
            list.add(new ArticleStore(pair.getKey(), pair.getValue()));
        }
        return list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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
        if (!(o instanceof ArticleStore article)) return false;
        return id == article.id && count == article.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, count);
    }
}