package ch.hslu.swda.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CentralWarehouseOrderJSONMapperTest {
    CentralWarehouseOrderJSONMapper mapper = new CentralWarehouseOrderJSONMapper();

    @Test
    void toCentralWarehouseOrderTest() {

        // Generate a future date, e.g., one month from the current date
        LocalDate futureDate = LocalDate.now().plusMonths(1);
        String futureDateString = futureDate.toString();

        String json = """
            {"id": "63dbbace-a29e-11ef-87e3-0242ac130007", 
             "storeId": "63dbbae9-a29e-11ef-87e3-0242ac130007", 
             "articles": [
                 {"count": 50, "articleId": 1002001, "fulfilled": 0, "nextDeliveryDate": null}, 
                 {"count": 10, "articleId": 1002002, "fulfilled": 1, "nextDeliveryDate": "%s"}
             ], 
             "cancelled": 0, 
             "customerOrderId": "63dbbaeb-a29e-11ef-87e3-0242ac130007"}
            """.formatted(futureDateString);

        CentralWarehouseOrder order = mapper.toCentralWarehouseOrder(json);

        Assertions.assertEquals(UUID.fromString("63dbbace-a29e-11ef-87e3-0242ac130007"), order.getId());
        Assertions.assertEquals(futureDateString, order.getArticles().get(1).getNextDeliveryDate().toString());
    }
    @Test
    void fromCentralWarehouseOrderTest(){
        UUID storeId = UUID.randomUUID();
        HashMap<Integer,Integer> articles = new HashMap<Integer,Integer>();
        articles.put(100000,50);
        CentralWarehouseOrder order = new CentralWarehouseOrder(storeId,articles);
        String res = mapper.fromCentralWarehouseOrder(order).toString();
        Assertions.assertTrue(res.contains("100000")&&res.contains("50")&&res.contains(storeId.toString()));
    }
}
