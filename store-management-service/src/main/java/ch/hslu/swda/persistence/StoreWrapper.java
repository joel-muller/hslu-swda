package ch.hslu.swda.persistence;

import ch.hslu.swda.entities.Order;
import ch.hslu.swda.entities.OrderArticle;
import ch.hslu.swda.entities.Store;
import ch.hslu.swda.entities.StoreArticle;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StoreWrapper {
    private final DBStore dbStore;

    public StoreWrapper(final Store store) {
        List<StoreArticle> storeArticles = store.getArticleList();
        List<DBStoreArticle> dbStoreArticles = new ArrayList<>();
        for (StoreArticle storeArticle : storeArticles) {
            dbStoreArticles.add(new DBStoreArticle(storeArticle.getId(), storeArticle.getActualQuantity(), storeArticle.getMinimumQuantity(), storeArticle.getRefillCount()));
        }
        List<Order> orders = store.getOpenOrders();
        List<DBOrder> dbOrders = new ArrayList<>();
        for (Order order : orders) {
            List<OrderArticle> orderArticles = order.getArticleOrderedList();
            List<DBOrderArticle> dbOrderArticles = new ArrayList<>();
            for (OrderArticle orderArticle : orderArticles) {
                dbOrderArticles.add(new DBOrderArticle(orderArticle.getId(), orderArticle.getCount(), orderArticle.isReady()));
            }
            dbOrders.add(new DBOrder(order.getId(), order.getStoreId(), dbOrderArticles));
        }
        this.dbStore = new DBStore(store.getId(), dbStoreArticles, dbOrders);
    }

    public StoreWrapper(final DBStore dbStore) {
        this.dbStore = dbStore;
    }

    public Store getStore() {
        List<DBStoreArticle> dbStoreArticles = dbStore.getArticleList();
        List<StoreArticle> storeArticles = new ArrayList<>();
        for (DBStoreArticle dbStoreArticle : dbStoreArticles) {
            storeArticles.add(new StoreArticle(dbStoreArticle.getId(), dbStoreArticle.getActualQuantity(), dbStoreArticle.getMinimumQuantity(), dbStoreArticle.getRefillCount()));
        }
        List<Order> orders = new ArrayList<>();
        List<DBOrder> dbOrders = this.dbStore.getOpenOrders();
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

    public DBStore getDbStore() {
        return dbStore;
    }
}
