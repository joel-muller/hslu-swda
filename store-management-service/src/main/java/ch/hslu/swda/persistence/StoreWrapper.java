package ch.hslu.swda.persistence;

import ch.hslu.swda.entities.Order;
import ch.hslu.swda.entities.OrderArticle;
import ch.hslu.swda.entities.Store;
import ch.hslu.swda.entities.StoreArticle;

import java.util.ArrayList;
import java.util.List;

public class StoreWrapper {

    public static DBStore createDBStore(final Store store) {
        List<StoreArticle> storeArticles = store.getCopyOfArticleList();
        List<DBStoreArticle> dbStoreArticles = new ArrayList<>();
        for (StoreArticle storeArticle : storeArticles) {
            dbStoreArticles.add(new DBStoreArticle(storeArticle.getId(), storeArticle.getActualQuantity(), storeArticle.getMinimumQuantity(), storeArticle.getRefillCount()));
        }
        List<Order> orders = store.getCopyOfOpenOrders();
        List<DBOrder> dbOrders = new ArrayList<>();
        for (Order order : orders) {
            List<OrderArticle> orderArticles = order.getCopyOfArticleOrderedList();
            List<DBOrderArticle> dbOrderArticles = new ArrayList<>();
            for (OrderArticle orderArticle : orderArticles) {
                dbOrderArticles.add(new DBOrderArticle(orderArticle.getId(), orderArticle.getCount(), orderArticle.isReady()));
            }
            dbOrders.add(new DBOrder(order.getId(), dbOrderArticles));
        }
        return new DBStore(store.getId(), dbStoreArticles, dbOrders);
    }


    public static Store getStore(DBStore dbStore) {
        List<DBStoreArticle> dbStoreArticles = dbStore.getArticleList();
        List<StoreArticle> storeArticles = new ArrayList<>();
        for (DBStoreArticle dbStoreArticle : dbStoreArticles) {
            storeArticles.add(new StoreArticle(dbStoreArticle.getId(), dbStoreArticle.getActualQuantity(), dbStoreArticle.getMinimumQuantity(), dbStoreArticle.getRefillCount()));
        }
        List<Order> orders = new ArrayList<>();
        List<DBOrder> dbOrders = dbStore.getOpenOrders();
        for (DBOrder dbOrder : dbOrders) {
            List<OrderArticle> orderArticles = new ArrayList<>();
            List<DBOrderArticle> dbOrderArticles = dbOrder.getArticleOrderedList();
            for (DBOrderArticle article : dbOrderArticles) {
                orderArticles.add(new OrderArticle(article.getId(), article.getCount(), article.isReady()));
            }
            orders.add(new Order(dbOrder.getId(), orderArticles));
        }
        return new Store(dbStore.getId(), storeArticles, orders);
    }

}
