package ch.hslu.swda.persistence;

import ch.hslu.swda.entities.Order;
import ch.hslu.swda.entities.OrderArticle;
import ch.hslu.swda.entities.Store;
import ch.hslu.swda.entities.StoreArticle;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StoreWrapper {
    private final List<DBOrder> dbOrders;
    private final DBStore dbStore;

    public StoreWrapper(final Store store) {
        List<StoreArticle> storeArticles = store.getArticleList();
        List<DBStoreArticle> dbStoreArticles = new ArrayList<>();
        for (StoreArticle storeArticle : storeArticles) {
            dbStoreArticles.add(new DBStoreArticle(storeArticle.getId(), storeArticle.getActualQuantity(), storeArticle.getMinimumQuantity(), storeArticle.getRefillCount()));
        }
        List<Order> orders = store.getOpenOrders();
        List<UUID> openOrders = new ArrayList<>();
        List<DBOrder> dbOrders = new ArrayList<>();
        for (Order order : orders) {
            List<OrderArticle> orderArticles = order.getArticleOrderedList();
            List<DBOrderArticle> dbOrderArticles = new ArrayList<>();
            for (OrderArticle orderArticle : orderArticles) {
                dbOrderArticles.add(new DBOrderArticle(orderArticle.getId(), orderArticle.getCount(), orderArticle.isReady()));
            }
            dbOrders.add(new DBOrder(order.getId(), order.getStoreId(), dbOrderArticles));
            openOrders.add(order.getId());
        }
        this.dbOrders = dbOrders;
        this.dbStore = new DBStore(store.getId(), dbStoreArticles, openOrders);
    }

    public StoreWrapper(final List<DBOrder> dbOrders, final DBStore dbStore) {
        this.dbStore = dbStore;
        this.dbOrders = dbOrders;
    }

    public Store getStore() {
        List<DBStoreArticle> dbStoreArticles = dbStore.getArticleList();
        List<StoreArticle> storeArticles = new ArrayList<>();
        for (DBStoreArticle dbStoreArticle : dbStoreArticles) {
            storeArticles.add(new StoreArticle(dbStoreArticle.getId(), dbStoreArticle.getActualQuantity(), dbStoreArticle.getMinimumQuantity(), dbStoreArticle.getRefillCount()));
        }
        List<Order> orders = new ArrayList<>();
        for (DBOrder dbOrder : dbOrders) {
            List<OrderArticle> orderArticles = new ArrayList<>();
            List<DBOrderArticle> dbOrderArticles = dbOrder.getArticleOrderedList();
            for (DBOrderArticle article : dbOrderArticles) {
                orderArticles.add(new OrderArticle(article.getId(), article.getCount(), article.isReady()));
            }
            orders.add(new Order(dbOrder.getId(), dbOrder.getStoreId(), orderArticles));
        }
        return new Store(this.dbStore.getId(), storeArticles, orders);
    }

    public List<DBOrder> getDbOrders() {
        return dbOrders;
    }

    public DBStore getDbStore() {
        return dbStore;
    }
}
