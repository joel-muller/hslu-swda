package ch.hslu.swda.persistence;

import ch.hslu.swda.entities.Article;
import ch.hslu.swda.entities.Order;
import ch.hslu.swda.entities.Price;
import ch.hslu.swda.entities.StateEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseWrapperTest {
    private Order order;
    private UUID orderId;
    private UUID storeId;
    private UUID customerId;
    private UUID employeeId;
    private List<Article> articles;
    private StateEnum stateEnum;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        storeId = UUID.randomUUID();
        customerId = UUID.randomUUID();
        employeeId = UUID.randomUUID();
        stateEnum = StateEnum.STORED;
        List<Article> articles = new ArrayList<>();
        Article article2 = new Article(2, 10, new Price(4, 50));
        Article article = new Article(1, 5, new Price(15, 10));
        Article anotherArticle = new Article(5, 44, new Price(6, 70));
        anotherArticle.setDelivered(true);
        articles.add(anotherArticle);
        articles.add(article);
        articles.add(article2);
        order = new Order(orderId, Calendar.getInstance().getTime(), storeId, customerId, employeeId, articles, stateEnum, false);
    }

    @Test
    void testWrapper() {
        DBOrder dbOrder = DatabaseWrapper.createDBOrder(order);
        Order restoredOrder = DatabaseWrapper.createOrder(dbOrder);
        assertEquals(orderId, restoredOrder.getId());
        assertEquals(storeId, restoredOrder.getStoreId());
        assertEquals(customerId, restoredOrder.getCustomerId());
        assertEquals(employeeId, restoredOrder.getEmployeeId());
        assertEquals(order.getCopyOfArticles().getFirst(), restoredOrder.getCopyOfArticles().getFirst());
        assertEquals(order.getCopyOfArticles().get(1), restoredOrder.getCopyOfArticles().get(1));
        assertEquals(order.getCopyOfArticles().getLast(), restoredOrder.getCopyOfArticles().getLast());
        assertEquals(order.getState(), restoredOrder.getState());
    }
}