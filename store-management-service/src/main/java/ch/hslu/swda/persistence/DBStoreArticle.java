package ch.hslu.swda.persistence;

import dev.morphia.annotations.Id;

public class DBStoreArticle {
    @Id
    private int id;
    private int actualQuantity;
    private int minimumQuantity;
    private int refillCount;


    public DBStoreArticle(final int id, final int actualQuantity, final int minimumQuantity, final int refillCount) {
        this.id = id;
        this.actualQuantity = actualQuantity;
        this.minimumQuantity = minimumQuantity;
        this.refillCount = refillCount;
    }

    public DBStoreArticle() {
        this.id = -1;
        this.actualQuantity = -1;
        this.minimumQuantity = -1;
        this.refillCount = -1;
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

    public void setActualQuantity(int actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    public int getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(int minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public int getRefillCount() {
        return refillCount;
    }

    public void setRefillCount(int refillCount) {
        this.refillCount = refillCount;
    }
}
