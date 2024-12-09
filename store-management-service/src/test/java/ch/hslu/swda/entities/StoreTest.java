package ch.hslu.swda.entities;

import ch.hslu.swda.messagesIngoing.NewOrder;
import ch.hslu.swda.messagesOutgoing.OrderUpdate;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class StoreTest {
    @Test
    void testConstructorWithParameters() {
        UUID id = UUID.randomUUID();
        List<StoreArticle> articles = List.of(new StoreArticle(1, 100, 10, 50));
        List<Order> orders = List.of(new Order(UUID.randomUUID(), new ArrayList<>()));

        Store store = new Store(id, articles, orders);

        assertEquals(id, store.getId());
        assertEquals(articles, store.getCopyOfArticleList());
        assertEquals(orders, store.getCopyOfOpenOrders());
    }

    @Test
    void testDefaultConstructor() {
        Store store = new Store();

        assertNotNull(store.getId());
        assertTrue(store.getCopyOfArticleList().isEmpty());
        assertTrue(store.getCopyOfOpenOrders().isEmpty());
    }

    @Test
    void testAddDefaultInventory() {
        Store store = new Store();

        store.addDefaultInventory();
        List<StoreArticle> inventory = store.getCopyOfArticleList();

        assertEquals(100, inventory.size());
        for (StoreArticle article : inventory) {
            assertTrue(article.getId() >= 100000);
        }
    }

    @Test
    void testAddArticle_NewArticle() {
        Store store = new Store();
        StoreArticle newArticle = new StoreArticle(1, 50, 10, 20);

        store.addArticle(newArticle);
        List<StoreArticle> articles = store.getCopyOfArticleList();

        assertEquals(1, articles.size());
        assertEquals(newArticle.getId(), articles.get(0).getId());
        assertEquals(newArticle.getActualQuantity(), articles.get(0).getActualQuantity());
    }

    @Test
    void testAddArticle_UpdateExistingArticle() {
        Store store = new Store();
        StoreArticle existingArticle = new StoreArticle(1, 30, 5, 10);
        store.addArticle(existingArticle);

        StoreArticle updatedArticle = new StoreArticle(1, 20, 8, 15);
        store.addArticle(updatedArticle);

        List<StoreArticle> articles = store.getCopyOfArticleList();
        assertEquals(1, articles.size());
        StoreArticle article = articles.get(0);

        assertEquals(1, article.getId());
        assertEquals(50, article.getActualQuantity()); // 30 + 20
        assertEquals(15, article.getRefillCount());
        assertEquals(8, article.getMinimumQuantity());
    }

    @Test
    void testRefillArticle_NewArticle() {
        Store store = new Store();
        store.refillArticle(1, 50);

        List<StoreArticle> articles = store.getCopyOfArticleList();

        assertEquals(1, articles.size());
        StoreArticle article = articles.get(0);

        assertEquals(1, article.getId());
        assertEquals(50, article.getActualQuantity());
        assertEquals(0, article.getRefillCount());
        assertEquals(0, article.getMinimumQuantity());
    }

    @Test
    void testRefillArticle_UpdateExistingArticle() {
        Store store = new Store();
        StoreArticle existingArticle = new StoreArticle(1, 30, 5, 10);
        store.addArticle(existingArticle);

        store.refillArticle(1, 20);

        List<StoreArticle> articles = store.getCopyOfArticleList();
        assertEquals(1, articles.size());
        StoreArticle article = articles.get(0);

        assertEquals(1, article.getId());
        assertEquals(50, article.getActualQuantity()); // 30 + 20
        assertEquals(10, article.getRefillCount());
        assertEquals(5, article.getMinimumQuantity());
    }

    @Test
    void testAddOrder() {
        Store store = new Store();
        Order order = new Order(UUID.randomUUID(), new ArrayList<>());

        store.addOrder(order);
        List<Order> openOrders = store.getCopyOfOpenOrders();

        assertEquals(1, openOrders.size());
        assertEquals(order, openOrders.get(0));
    }

    @Test
    void testRemoveOrder() {
        Store store = new Store();
        UUID orderId = UUID.randomUUID();
        Order order = new Order(orderId, new ArrayList<>());

        store.addOrder(order);
        assertEquals(1, store.getCopyOfOpenOrders().size());

        store.removeOrder(orderId);
        assertTrue(store.getCopyOfOpenOrders().isEmpty());
    }

    @Test
    void testUpdateOrderNotValidID() {
        Map<Integer, Integer> orders = new HashMap<>();
        orders.put(1, 6);
        orders.put(2, 30);
        Store store = new Store();
        assertEquals(new OrderProcessed(new HashMap<>(), new ArrayList<>()), store.updateOrderStore(UUID.randomUUID(), orders));
    }

    @Test
    void testNewOrderEverythingHereSomethingHasToGetReordered() {
        Store store = new Store();
        StoreArticle article = new StoreArticle(1, 10, 5, 100);
        StoreArticle article2 = new StoreArticle(2, 30, 0, 6);
        store.addArticle(article);
        store.addArticle(article2);

        Map<Integer, Integer> orders = new HashMap<>();
        orders.put(1, 6);
        orders.put(2, 30);

        OrderProcessed processed = store.newOrder(UUID.randomUUID(), orders);

        assertEquals(2, processed.articlesReady().size());
        assertEquals(1, processed.articlesHaveToGetOrdered().size());
        assertEquals(1, processed.articlesReady().getFirst());
        assertEquals(2, processed.articlesReady().getLast());
        assertEquals(100, processed.articlesHaveToGetOrdered().get(1));
    }

    @Test
    void testNewOrderEverythingHerNotingHasToGetReordered() {
        Store store = new Store();
        StoreArticle article = new StoreArticle(1, 10, 5, 100);
        StoreArticle article2 = new StoreArticle(2, 30, 0, 6);
        store.addArticle(article);
        store.addArticle(article2);

        Map<Integer, Integer> orders = new HashMap<>();
        orders.put(1, 4);
        orders.put(2, 30);

        OrderProcessed processed = store.newOrder(UUID.randomUUID(), orders);

        assertEquals(2, processed.articlesReady().size());
        assertEquals(0, processed.articlesHaveToGetOrdered().size());
        assertEquals(1, processed.articlesReady().getFirst());
        assertEquals(2, processed.articlesReady().getLast());
    }

    @Test
    void testNewOrderNotEverythingIsHereSomethingHasToGetReordered() {
        Store store = new Store();
        StoreArticle article2 = new StoreArticle(2, 30, 0, 6);
        store.addArticle(article2);

        Map<Integer, Integer> orders = new HashMap<>();
        orders.put(1, 4);
        orders.put(2, 30);

        OrderProcessed processed = store.newOrder(UUID.randomUUID(), orders);

        assertEquals(1, processed.articlesReady().size());
        assertEquals(1, processed.articlesHaveToGetOrdered().size());
        assertEquals(2, processed.articlesReady().getFirst());
        assertEquals(4, processed.articlesHaveToGetOrdered().get(1));
    }

    @Test
    void testNewOrderNotEnoughOfSomething() {
        Store store = new Store();
        StoreArticle article = new StoreArticle(1, 4, 0, 2);
        StoreArticle article2 = new StoreArticle(2, 30, 0, 6);
        store.addArticle(article2);
        store.addArticle(article);

        Map<Integer, Integer> orders = new HashMap<>();
        orders.put(1, 8);
        orders.put(2, 30);

        OrderProcessed processed = store.newOrder(UUID.randomUUID(), orders);

        assertEquals(1, processed.articlesReady().size());
        assertEquals(1, processed.articlesHaveToGetOrdered().size());
        assertEquals(2, processed.articlesReady().getFirst());
        assertEquals(8, processed.articlesHaveToGetOrdered().get(1));
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        List<StoreArticle> articles = List.of(new StoreArticle(1, 100, 10, 50));
        List<Order> orders = List.of(new Order(UUID.randomUUID(), new ArrayList<>()));

        Store store1 = new Store(id, articles, orders);
        Store store2 = new Store(id, articles, orders);

        assertEquals(store1, store2);
        assertEquals(store1.hashCode(), store2.hashCode());
    }

    @Test
    void testToString() {
        Store store = new Store();
        String str = store.toString();

        assertTrue(str.contains("id"));
        assertTrue(str.contains("articleList"));
        assertTrue(str.contains("openOrders"));
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.simple().forClass(Store.class)
                .verify();
    }
}