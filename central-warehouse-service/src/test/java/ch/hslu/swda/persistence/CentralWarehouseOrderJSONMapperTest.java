package ch.hslu.swda.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ch.hslu.swda.entities.CentralWarehouseOrder;
import ch.hslu.swda.entities.CentralWarehouseOrderJSONMapper;
import ch.hslu.swda.entities.OrderArticle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public class CentralWarehouseOrderJSONMapperTest {
    CentralWarehouseOrderJSONMapper jsonMapper = new CentralWarehouseOrderJSONMapper();
    @Test
    void loadFromJSONTest() {
        String JSON= """
        {"id": "63dbbace-a29e-11ef-87e3-0242ac130007", "storeId": "63dbbae9-a29e-11ef-87e3-0242ac130008", "articles": [{"count": 50, "articleId": 1002001, "fulfilled": 0, "nextDeliveryDate": null}, {"count": 10, "articleId": 1002002, "fulfilled": 1, "nextDeliveryDate": "2024-12-01"}], "cancelled": 0, "customerOrderId": "63dbbaeb-a29e-11ef-87e3-0242ac130009"}
        """;
        CentralWarehouseOrder order = jsonMapper.toCentralWarehouseOrder(JSON);
        Assertions.assertEquals(UUID.fromString("63dbbace-a29e-11ef-87e3-0242ac130007"), order.getId());
        Assertions.assertEquals(UUID.fromString("63dbbae9-a29e-11ef-87e3-0242ac130008"), order.getStoreId());
        Assertions.assertEquals(UUID.fromString("63dbbaeb-a29e-11ef-87e3-0242ac130009"), order.getCustomerOrderId());
        Assertions.assertFalse(order.getCancelled());
        Assertions.assertEquals(1002001,order.getArticles().getFirst().getId());
        Assertions.assertEquals(50,order.getArticles().getFirst().getCount());
        Assertions.assertEquals(0,order.getArticles().getFirst().getFulfilled());
        Assertions.assertNull(order.getArticles().getFirst().getNextDeliveryDate());
        Assertions.assertEquals(LocalDate.parse("2024-12-01"),order.getArticles().get(1).getNextDeliveryDate());
    }

    @Test
    void toJSONTest(){
        ArrayList<OrderArticle> articleList = new ArrayList<OrderArticle>();
        articleList.add(new OrderArticle(1002001,50,0,null));
        CentralWarehouseOrder order = new CentralWarehouseOrder(
                UUID.fromString("63dbbae9-a29e-11ef-87e3-0242ac130007"),
                UUID.fromString("63dbbae9-a29e-11ef-87e3-0242ac130008"),
                UUID.fromString("63dbbae9-a29e-11ef-87e3-0242ac130009"),
                false,
                articleList);

        String result = jsonMapper.fromCentralWarehouseOrder(order);
        Assertions.assertTrue(result.contains("\"id\":\"63dbbae9-a29e-11ef-87e3-0242ac130007\""));

        Assertions.assertTrue(result.contains("\"storeId\":\"63dbbae9-a29e-11ef-87e3-0242ac130008\""));
        Assertions.assertTrue(result.contains(
                "\"articles\":["));
    }
}
