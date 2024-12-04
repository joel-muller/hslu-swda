package ch.hslu.swda.entities;


import java.util.*;

public final class Article {
    private final int id;
    private final int count;
    private boolean delivered;
    private Price price;

    public Article(final int id, final int count) {
        this(id, count, new Price(0, 0));
    }

    public Article(final int id, final int count, final Price price) {
        this.id = id;
        this.count = count;
        this.delivered = false;
        this.price = new Price(price.getFrancs(), price.getCentimes());
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

    public void setPrice(int francs, int centimes) {
        this.price = new Price(francs, centimes);
    }

    public Price getPrice() {
        return new Price(this.price.getFrancs(), this.price.getCentimes());
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", count=" + count +
                ", delivered=" + delivered +
                ", price=" + price.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return id == article.id && count == article.count && delivered == article.delivered && Objects.equals(price, article.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, count, delivered, price);
    }
}
