package ch.hslu.swda.entities;

import ch.hslu.swda.messagesIngoing.NewOrder;

import java.util.*;

public final class Store {
    final private UUID id;
    private final List<StoreArticle> articleList;
    private final List<Order> openOrders;

    public Store(final UUID id, List<StoreArticle> articleList, List<Order> openOrders) {
        this.id = id;
        this.articleList = StoreArticle.getCopyOfList(articleList);
        this.openOrders = Order.getCopyOfOrderList(openOrders);
    }

    public Store() {
        this.id = UUID.randomUUID();
        this.articleList = new ArrayList<>();
        this.openOrders = new ArrayList<>();
    }

    public List<StoreArticle> getCopyOfArticleList() {
        return StoreArticle.getCopyOfList(articleList);
    }

    public void addDefaultInventory() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            articleList.add(new StoreArticle((100000 + random.nextInt(100) + (i * 1000)), 100, 10, 100));
        }
    }

    public List<Order> getCopyOfOpenOrders() {
        return Order.getCopyOfOrderList(openOrders);
    }

    public void addOrder(Order order) {
        openOrders.add(order.getCopy());
    }

    public void removeOrder(UUID orderId) {
        openOrders.removeIf(order -> Objects.equals(order.getId(), orderId));
    }

    public void cancelOrder(UUID orderId) {

    }

    private StoreArticle getArticle(int id) {
        for (StoreArticle article : articleList) {
            if (article.getId() == id) {
                return article;
            }
        }
        return null;
    }

    public UUID getId() {
        return id;
    }

    public OrderProcessed newOrder(NewOrder newOrder) {
        Order order = Order.createFromOrderRequest(newOrder);
        addOrder(order);
        Map<Integer, Integer> articleHaveToBeOrdered = new HashMap<>();
        List<Integer> articleReserved = new ArrayList<>();
        for (OrderArticle article : order.getCopyOfArticleOrderedList()) {
            StoreArticle storeArticle = getArticle(article.getId());
            if (storeArticle == null) {
                articleHaveToBeOrdered.put(article.getId(), article.getCount());
            } else {
                int refillBack = storeArticle.getWithRefillBack(article.getCount());
                if (refillBack < 0) {
                    // not enough count of article here
                    articleHaveToBeOrdered.put(article.getId(), article.getCount());
                } else if (refillBack == 0) {
                    // article is reserved, no refill needed
                    article.setReady(true);
                    articleReserved.add(article.getId());
                } else {
                    // article is reserved but a refill is needed
                    article.setReady(true);
                    articleReserved.add(article.getId());
                    articleHaveToBeOrdered.put(article.getId(), refillBack);
                }
            }
        }
        return new OrderProcessed(articleHaveToBeOrdered, articleReserved);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Store store))
            return false;
        return Objects.equals(id, store.id) && Objects.equals(articleList, store.articleList)
                && Objects.equals(openOrders, store.openOrders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, articleList, openOrders);
    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", articleList=" + articleList +
                ", openOrders=" + openOrders +
                '}';
    }
}
