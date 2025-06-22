package ch.hslu.swda.business;

import ch.hslu.swda.entities.Order;
import ch.hslu.swda.entities.OrderArticle;
import ch.hslu.swda.entities.Store;
import ch.hslu.swda.entities.StoreArticle;
import ch.hslu.swda.persistence.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FakeDatabaseConnector implements Data {
    UUID storeId;
    UUID orderId;
    Store lastSavedStore;

    public FakeDatabaseConnector(UUID storeId, UUID orderId) {
        this.storeId = storeId;
        this.orderId = orderId;
    }

    @Override
    public Store getStore(UUID storeId) {
        if (!Objects.equals(this.storeId, storeId)) {
            return null;
        }
        List<OrderArticle> orderArticles = new ArrayList<>();
        orderArticles.add(new OrderArticle(3, 50, true));
        orderArticles.add(new OrderArticle(4, 4, false));
        Order order = new Order(this.orderId, orderArticles);
        List<Order> orders = new ArrayList<>();
        orders.add(order);

        List<StoreArticle> articles = new ArrayList<>();
        articles.add(new StoreArticle(1, 10, 5, 10));
        articles.add(new StoreArticle(2, 20, 3, 45));
        articles.add(new StoreArticle(3, 50, 0, 0));
        return new Store(storeId, articles, orders);
    }

    @Override
    public void storeStore(Store store) {
        this.lastSavedStore = store;
    }

    @Override
    public List<Store> getAllStores() {
        return List.of();
    }
}
