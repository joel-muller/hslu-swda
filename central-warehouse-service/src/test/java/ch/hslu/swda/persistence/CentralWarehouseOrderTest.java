package ch.hslu.swda.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import ch.hslu.swda.entities.CentralWarehouseOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
public class CentralWarehouseOrderTest {

    @Test
    void loadFromJSONTest()throws Exception{
        String JSON= """
        {"id": "63dbbace-a29e-11ef-87e3-0242ac130007", "storeId": "63dbbae9-a29e-11ef-87e3-0242ac130007", "articles": [{"count": 50, "articleId": 1002001, "fulfilled": 0, "nextDeliveryDate": null}, {"count": 10, "articleId": 1002002, "fulfilled": 1, "nextDeliveryDate": "2024-12-01"}], "cancelled": 0, "customerOrderId": "63dbbaeb-a29e-11ef-87e3-0242ac130007"}
        """;
    }

}
