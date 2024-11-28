package ch.hslu.swda.entities;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class StoreArticle {
    private final int id;
    private int actualQuantity;
    private int minimumQuantity;
    private int refillCount;


    public StoreArticle() {
        this.id = 0;
        this.actualQuantity = 0;
        this.minimumQuantity = 0;
        this.refillCount = 0;
    }

    public StoreArticle(final int id, final int actualQuantity, final int minimumQuantity, final int refillCount) {
        this.id = id;
        this.actualQuantity = actualQuantity;
        this.minimumQuantity = minimumQuantity;
        this.refillCount = refillCount;
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

    public void decrementQuantity(int count) {
        this.actualQuantity -= count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoreArticle article)) return false;
        return id == article.id && actualQuantity == article.actualQuantity && minimumQuantity == article.minimumQuantity && refillCount == article.refillCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, actualQuantity, minimumQuantity, refillCount);
    }

    @Override
    public String toString() {
        return "StoreArticle{" +
                "id=" + id +
                ", actualQuantity=" + actualQuantity +
                ", minimumQuantity=" + minimumQuantity +
                ", refillCount=" + refillCount +
                '}';
    }
}