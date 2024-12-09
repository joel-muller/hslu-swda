package ch.hslu.swda.persistence;

import ch.hslu.swda.entities.Order;
import ch.hslu.swda.entities.OrderArticle;
import ch.hslu.swda.entities.Store;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StoreWrapperTest {

    @Test
    void createDBStore() {
        UUID orderId = UUID.randomUUID();
        List<OrderArticle> orderArticleList = new ArrayList<>();
        orderArticleList.add(new OrderArticle(1, 4, true));
        orderArticleList.add(new OrderArticle(5, 6, false));
        Store store = new Store();
        store.addDefaultInventory();
        store.addOrder(new Order(orderId, orderArticleList));
        DBStore dbStore = StoreWrapper.createDBStore(store);
        Store newStore = StoreWrapper.getStore(dbStore);
    }
}