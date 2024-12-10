package ch.hslu.swda.entities;

import ch.hslu.swda.messagesIngoing.NewOrder;
import ch.hslu.swda.messagesOutgoing.OrderUpdate;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class StoreTest {
    Store store;
    UUID storeId;
    List<StoreArticle> articles;
    List<OrderArticle> orderArticles;
    Order order;
    List<Order> orders;
    UUID orderId;


    @BeforeEach
    void setup() {
        orderId = UUID.randomUUID();
        orderArticles = new ArrayList<>();
        orderArticles.add(new OrderArticle(3, 50, false));
        orderArticles.add(new OrderArticle(4, 4, true));
        order = new Order(orderId, orderArticles);
        orders = new ArrayList<>();
        orders.add(order);

        storeId = UUID.randomUUID();
        articles = new ArrayList<>();
        articles.add(new StoreArticle(1, 10, 5, 10));
        articles.add(new StoreArticle(2, 20, 3, 45));
        articles.add(new StoreArticle(3, 50, 0, 0));

        store = new Store(storeId, articles, orders);
    }

    @Test
    void testConstructorWithParameters() {
        assertEquals(storeId, store.getId());
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
        StoreArticle newArticle = new StoreArticle(4, 50, 10, 20);
        store.addArticle(newArticle);
        List<StoreArticle> articles = store.getCopyOfArticleList();

        assertEquals(4, articles.size());
        assertEquals(newArticle.getId(), articles.get(3).getId());
        assertEquals(newArticle.getActualQuantity(), articles.get(3).getActualQuantity());
        assertEquals(newArticle.getMinimumQuantity(), articles.get(3).getMinimumQuantity());
        assertEquals(newArticle.getRefillCount(), articles.get(3).getRefillCount());
    }

    @Test
    void testAddArticle_UpdateExistingArticle() {
        StoreArticle updatedArticle = new StoreArticle(1, 20, 8, 15);
        store.addArticle(updatedArticle);

        List<StoreArticle> articles = store.getCopyOfArticleList();
        assertEquals(3, articles.size());
        StoreArticle article = articles.getFirst();

        assertEquals(1, article.getId());
        assertEquals(30, article.getActualQuantity()); // 10 + 20
        assertEquals(15, article.getRefillCount());
        assertEquals(8, article.getMinimumQuantity());
    }

    @Test
    void testRefillArticle_NewArticle() {
        store.refillArticle(10, 99);

        assertEquals(4, store.getCopyOfArticleList().size());
        StoreArticle article = store.getCopyOfArticleList().get(3);

        assertEquals(10, article.getId());
        assertEquals(99, article.getActualQuantity());
        assertEquals(0, article.getRefillCount());
        assertEquals(0, article.getMinimumQuantity());
    }

    @Test
    void testRefillArticle_UpdateExistingArticle() {
        store.refillArticle(2, 99);

        assertEquals(3, store.getCopyOfArticleList().size());
        StoreArticle article = store.getCopyOfArticleList().get(1);

        assertEquals(2, article.getId());
        assertEquals(119, article.getActualQuantity());
        assertEquals(45, article.getRefillCount());
        assertEquals(3, article.getMinimumQuantity());

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
        Store store = new Store();
        UUID orderId = UUID.randomUUID();
        assertEquals(new OrderProcessed(orderId, new HashMap<>(), new ArrayList<>()), store.updateOrderStore(orderId));
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

        List<StoreArticle> articles = store.getCopyOfArticleList();
        assertEquals(new StoreArticle(1, 4, 5, 100), articles.getFirst());
        assertEquals(new StoreArticle(2, 0, 0, 6), articles.getLast());
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

    @Test
    void testCancelOrder() {
        Store store = new Store();
        StoreArticle article = new StoreArticle(1, 20, 0, 2);
        StoreArticle article2 = new StoreArticle(2, 30, 0, 6);
        store.addArticle(article);
        store.addArticle(article2);

        Map<Integer, Integer> orders = new HashMap<>();
        orders.put(1, 8);
        orders.put(2, 30);

        UUID orderId = UUID.randomUUID();

        store.newOrder(orderId, orders);

        store.cancelOrder(orderId);

        List<StoreArticle> articles = store.getCopyOfArticleList();
        assertEquals(new StoreArticle(1, 20, 0, 2), articles.get(0));
        assertEquals(new StoreArticle(2, 30, 0, 6), articles.get(1));
        assertEquals(0, store.getCopyOfOpenOrders().size());
    }

    @Test
    void handleInventoryUpdate() {
        Store store = new Store();
        StoreArticle article = new StoreArticle(1, 20, 0, 2);
        StoreArticle article2 = new StoreArticle(2, 30, 0, 6);
        store.addArticle(article);
        store.addArticle(article2);

        Map<Integer, Integer> orders = new HashMap<>();
        orders.put(1, 8);
        orders.put(2, 100);

        UUID orderId = UUID.randomUUID();

        store.newOrder(orderId, orders);

        Map<Integer, Integer> invArticles = new HashMap<>();
        invArticles.put(2, 100);
        invArticles.put(4, 55);
        OrderProcessed processed = store.handleInventoryUpdate(orderId, invArticles);


        List<StoreArticle> articles = store.getCopyOfArticleList();
        assertEquals(new StoreArticle(1, 12, 0, 2), articles.get(0));
        assertEquals(new StoreArticle(2, 30, 0, 6), articles.get(1));
        assertEquals(new StoreArticle(4, 55, 0, 0), articles.get(2));
        assertTrue(store.getCopyOfOpenOrders().getFirst().getCopy().isReady());
        assertEquals(2, processed.articlesReady().getFirst());
    }

    @Test
    void handleInventoryUpdate2() {
        Store store = new Store();
        StoreArticle article = new StoreArticle(1, 20, 0, 2);
        StoreArticle article2 = new StoreArticle(2, 30, 0, 6);
        store.addArticle(article);
        store.addArticle(article2);

        Map<Integer, Integer> orders = new HashMap<>();
        orders.put(1, 8);
        orders.put(4, 10);

        UUID orderId = UUID.randomUUID();

        store.newOrder(orderId, orders);

        Map<Integer, Integer> invArticles = new HashMap<>();
        invArticles.put(2, 100);
        invArticles.put(4, 55);
        OrderProcessed processed = store.handleInventoryUpdate(orderId, invArticles);


        List<StoreArticle> articles = store.getCopyOfArticleList();
        assertEquals(new StoreArticle(1, 12, 0, 2), articles.get(0));
        assertEquals(new StoreArticle(2, 130, 0, 6), articles.get(1));
        assertEquals(new StoreArticle(4, 45, 0, 0), articles.get(2));
        assertTrue(store.getCopyOfOpenOrders().getFirst().getCopy().isReady());
        assertEquals(4, processed.articlesReady().getFirst());
    }

    @Test
    void handleInventoryUpdateNoOrder() {
        Store store = new Store();
        StoreArticle article = new StoreArticle(1, 20, 0, 2);
        StoreArticle article2 = new StoreArticle(2, 30, 0, 6);
        store.addArticle(article);
        store.addArticle(article2);
        Map<Integer, Integer> invArticles = new HashMap<>();
        invArticles.put(2, 100);
        invArticles.put(44, 55345);
        store.handleInventoryUpdate(UUID.randomUUID(), invArticles);
        List<StoreArticle> articles = store.getCopyOfArticleList();
        assertEquals(new StoreArticle(1, 20, 0, 2), articles.get(0));
        assertEquals(new StoreArticle(2, 130, 0, 6), articles.get(1));
        assertEquals(new StoreArticle(44, 55345, 0, 0), articles.get(2));
    }
}