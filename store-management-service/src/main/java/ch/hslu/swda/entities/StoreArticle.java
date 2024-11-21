package ch.hslu.swda.entities;

import dev.morphia.annotations.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Represents an article in the store with its quantities.
 * 
 * <p>This entity is mapped to the "articleStore" table in the database.</p>
 * 
 * <p>Attributes:</p>
 * <ul>
 *   <li>id - The unique identifier for the article.</li>
 *   <li>actualQuantity - The current quantity of the article in the store.</li>
 *   <li>minimumQuantity - The minimum quantity of the article that should be maintained in the store.</li>
 *   <li>reservedQuantity - The quantity of the article that is reserved.</li>
 * </ul>
 * 
 * <p>Constructors:</p>
 * <ul>
 *   <li>{@link #StoreArticle(int, int, int, int)} - Initializes a new instance with specified values.</li>
 *   <li>{@link #StoreArticle()} - Initializes a new instance with default values (-1).</li>
 * </ul>
 * 
 * <p>Methods:</p>
 * <ul>
 *   <li>{@link #createArticleList(Map)} - Creates a list of StoreArticle instances from a map of id and actualQuantity.</li>
 *   <li>{@link #getId()} - Gets the id of the article.</li>
 *   <li>{@link #setId(int)} - Sets the id of the article.</li>
 *   <li>{@link #getActualQuantity()} - Gets the actual quantity of the article.</li>
 *   <li>{@link #setActualQuantity(int)} - Sets the actual quantity of the article.</li>
 *   <li>{@link #getMinimumQuantity()} - Gets the minimum quantity of the article.</li>
 *   <li>{@link #setMinimumQuantity(int)} - Sets the minimum quantity of the article.</li>
 *   <li>{@link #getReservedQuantity()} - Gets the reserved quantity of the article.</li>
 *   <li>{@link #setReservedQuantity(int)} - Sets the reserved quantity of the article.</li>
 *   <li>{@link #toString()} - Returns a string representation of the article.</li>
 *   <li>{@link #equals(Object)} - Checks if this article is equal to another object.</li>
 *   <li>{@link #hashCode()} - Returns the hash code for this article.</li>
 * </ul>
 */
@Entity("StoreArticle")
public class StoreArticle {
    private int id;
    private int actualQuantity;
    private int minimumQuantity;
    private int reservedQuantity;


    public StoreArticle(final int id, final int actualQuantity, final int minimumQuantity, final int reservedQuantity) {
        this.id = id;
        this.actualQuantity = actualQuantity;
        this.minimumQuantity = minimumQuantity;
        this.reservedQuantity = reservedQuantity;
    }

    public StoreArticle() {
        this.id = -1;
        this.actualQuantity = -1;
        this.minimumQuantity = -1;
        this.reservedQuantity = -1;
    }

    public static List<StoreArticle> createArticleList(Map<Integer, Integer> map) {
        ArrayList<StoreArticle> list = new ArrayList<>();
        for (Map.Entry<Integer, Integer> pair : map.entrySet()) {
            list.add(new StoreArticle(pair.getKey(), pair.getValue(), -1, -1)); //Achtung: minimumQuantity und reservedQuantity sind nicht gesetzt
        }
        return list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(int count) {
        this.actualQuantity = count;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", actualQuantity=" + actualQuantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoreArticle article)) return false;
        return id == article.id && actualQuantity == article.actualQuantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, actualQuantity);
    }

    public int getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(int minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public int getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(int reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }
}