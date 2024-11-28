package ch.hslu.swda.entities;


import java.util.*;

public class Article {
    private final int id;
    private final int count;
    private boolean delivered;

    public Article(final int id, final int count) {
        this.id = id;
        this.count = count;
        this.delivered = false;
    }


    public static List<Article> createListArticle(Map<Integer, Integer> map) {
        ArrayList<Article> list = new ArrayList<>();
        for (Map.Entry<Integer, Integer> pair : map.entrySet()) {
            list.add(new Article(pair.getKey(), pair.getValue()));
        }
        return list;
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

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", count=" + count +
                ", delivered=" + delivered +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return id == article.id && count == article.count && delivered == article.delivered;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, count, delivered);
    }
}
