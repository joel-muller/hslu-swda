package ch.hslu.swda.persistence;

import ch.hslu.swda.entities.CentralWarehouseOrder;
import ch.hslu.swda.entities.OrderArticle;
import ch.hslu.swda.micro.CentralWarehouseService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.UUID;

public class MysqlDatabasePersistorTest {

    private static final Logger LOG = LoggerFactory.getLogger(MysqlDatabasePersistorTest.class);
    private static final String IMAGE = "mysql:latest";

    private CentralWarehouseOrder order;

    Connection sqlConnection;
    MysqlDatabasePersistor persistor;

    @Container
    private final GenericContainer<?> container
            = new GenericContainer<>(DockerImageName.parse(IMAGE))
            .withExposedPorts(3306)
            .withCopyFileToContainer(MountableFile.forHostPath("./config/mysql/central_warehouse.sql"),"/docker-entrypoint-initdb.d/central_warehouse.sql")
            .withStartupTimeout(Duration.ofSeconds(60))
            .withEnv("MYSQL_ROOT_PASSWORD", "root");

    void init(){
        container.start();
        LOG.info("exposed Port: "+container.getMappedPort(3306).toString());
        String jdbcUrl = "jdbc:mysql://localhost:"+ container.getMappedPort(3306) +"/central_warehouse?serverTimezone=UTC";
        Connection sqlConnection;
        try{

            LOG.debug("Try establishing database connection. url:"+ jdbcUrl+" user: swda");
            sqlConnection = DriverManager.getConnection(jdbcUrl,"swda","swda");
            LOG.info("Connected to Database");
        }
        catch (SQLException e){
            LOG.info("Could not connect to Database");
            throw new RuntimeException(e);

        }

        ArrayList<OrderArticle> articleList = new ArrayList<OrderArticle>();
        articleList.add(new OrderArticle(1002001,50,0,null));
        this.order = new CentralWarehouseOrder(
                UUID.fromString("63dbbae9-a29e-11ef-87e3-0242ac130007"),
                UUID.fromString("63dbbae9-a29e-11ef-87e3-0242ac130008"),
                UUID.fromString("63dbbae9-a29e-11ef-87e3-0242ac130009"),
                false,
                articleList);

        persistor = new MysqlDatabasePersistor(sqlConnection);
    }

    @Test
    void persistorCheckSaveOrderTest(){
        init();

        try {
            persistor.save(this.order);
        }catch (IOException e){
        LOG.info(e.getMessage());
        }
    }
}
