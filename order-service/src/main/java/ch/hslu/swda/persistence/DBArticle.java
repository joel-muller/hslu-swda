package ch.hslu.swda.persistence;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("article")
public class DBArticle {
    private final int id;
    private final int count;
    private boolean delivered;
    private int francs;
    private int centimes;

    public DBArticle(final int id, final int count, final boolean delivered, final int francs, final int centimes) {
        this.id = id;
        this.count = count;
        this.delivered = false;
        this.francs = francs;
        this.centimes = centimes;
    }

    public DBArticle() {
        this.id = 0;
        this.count = 0;
        this.delivered = false;
        this.francs = 0;
        this.centimes = 0;
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


    public int getFrancs() {
        return francs;
    }

    public int getCentimes() {
        return centimes;
    }

    public void setFrancs(int francs) {
        this.francs = francs;
    }

    public void setCentimes(int centimes) {
        this.centimes = centimes;
    }
}
