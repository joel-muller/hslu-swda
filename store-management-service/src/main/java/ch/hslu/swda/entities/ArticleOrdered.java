package ch.hslu.swda.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents an article in the context of an order.
 * This entity contains information about the article's ID, count, and readiness status.
 * It provides methods to create a list of ordered articles from a map, as well as getters and setters for its fields.
 * 
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * Map<Integer, Integer> map = new HashMap<>();
 * map.put(1, 10);
 * map.put(2, 20);
 * List<ArticleOrdered> articles = ArticleOrdered.createListArticle(map);
 * }
 * </pre>
 * 
 * <p>Attributes:</p>
 * <ul>
 *   <li>id - The unique identifier of the article.</li>
 *   <li>count - The number of articles ordered.</li>
 *   <li>ready - The readiness status of the article.</li>
 * </ul>
 * 
 * <p>Methods:</p>
 * <ul>
 *   <li>{@code createListArticle(Map<Integer, Integer> map)} - Creates a list of ArticleOrdered objects from a map.</li>
 *   <li>{@code setId(int id)} - Sets the ID of the article.</li>
 *   <li>{@code setCount(int count)} - Sets the count of the article.</li>
 *   <li>{@code getId()} - Gets the ID of the article.</li>
 *   <li>{@code getCount()} - Gets the count of the article.</li>
 *   <li>{@code isReady()} - Checks if the article is ready.</li>
 *   <li>{@code setReady(boolean ready)} - Sets the readiness status of the article.</li>
 *   <li>{@code toString()} - Returns a string representation of the article.</li>
 *   <li>{@code equals(Object o)} - Checks if this article is equal to another object.</li>
 *   <li>{@code hashCode()} - Returns the hash code of the article.</li>
 * </ul>
 */
@Entity("articleOrdered")
public class ArticleOrdered {
    @Id
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
