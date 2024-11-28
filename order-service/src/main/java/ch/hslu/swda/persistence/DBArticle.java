package ch.hslu.swda.persistence;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("article")
public class DBArticle {
    @Id
    private final int id;
    private final int count;
    private boolean delivered;

    public DBArticle(final int id, final int count, final boolean delivered) {
        this.id = id;
        this.count = count;
        this.delivered = false;
    }

    public DBArticle() {
        this.id = 0;
        this.count = 0;
        this.delivered = false;
    }

    public int getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }
}
