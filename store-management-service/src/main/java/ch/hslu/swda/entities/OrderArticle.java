package ch.hslu.swda.entities;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OrderArticle {
    private final int id;
    private final int count;
    private boolean ready;

    public OrderArticle(final int id, final int count, final boolean ready) {
        this.id = id;
        this.count = count;
        this.ready = ready;
    }

    public static List<OrderArticle> createListArticle(Map<Integer, Integer> map) {
        ArrayList<OrderArticle> list = new ArrayList<>();
        for (Map.Entry<Integer, Integer> pair : map.entrySet()) {
            list.add(new OrderArticle(pair.getKey(), pair.getValue(), false));
        }
        return list;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderArticle that)) return false;
        return id == that.id && count == that.count && ready == that.ready;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, count, ready);
    }

    @Override
    public String toString() {
        return "OrderArticle{" +
                "id=" + id +
                ", count=" + count +
                ", ready=" + ready +
                '}';
    }
}
