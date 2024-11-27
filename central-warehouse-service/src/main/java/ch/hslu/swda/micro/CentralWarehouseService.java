/*
 * Copyright 2024 Roland Christen, HSLU Informatik, Switzerland
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.RabbitMqConfig;

import ch.hslu.swda.persistence.MysqlDatabasePersistor;
import ch.hslu.swda.stock.api.Stock;
import ch.hslu.swda.stock.api.StockFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;


/**
 * Beispielcode für Implementation eines Servcies mit RabbitMQ.
 */
public final class CentralWarehouseService implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(CentralWarehouseService.class);
    private final String exchangeName;
    private final BusConnector bus;
    private final Stock stock;
    private final MysqlDatabasePersistor persistor;

    private final CentralWarehouseOrderManager orderManager;


    /**
     * @throws IOException      IO-Fehler.
     * @throws TimeoutException Timeout.
     */
    CentralWarehouseService() throws IOException, TimeoutException {
        // thread info
        String threadName = Thread.currentThread().getName();
        LOG.debug("[Thread: {}] Service started", threadName);

        //Get Database settings from env
        final String DATABASEHOST = System.getenv("DATABASEHOST");
        LOG.info("READ DATABASEHOST: "+DATABASEHOST);
        final String DATABASEPORT = System.getenv("DATABASEPORT");
        LOG.info("READ DATABASEPORT: "+DATABASEPORT);

        final String DATABASEUSER = System.getenv("DATABASEUSER");
        LOG.info("READ DATABASEUSER: "+DATABASEUSER);
        final String DATABASEPASSWORD = System.getenv("DATABASEPASSWORD");
        LOG.debug("READ DATABASEPASSWORD: "+DATABASEPASSWORD);

        final String DATABASENAME = System.getenv("DATABASENAME");

        String jdbcUrl = "jdbc:mysql://"+DATABASEHOST+":"+DATABASEPORT+"/"+DATABASENAME+"?serverTimezone=UTC";
        Connection sqlConnection;

        int retryCount = 0;
        int delaySeconds = 5;

        while(true) {
            try {

                LOG.debug("Try establishing database connection. url:" + jdbcUrl + " user: " + DATABASEUSER);
                sqlConnection = DriverManager.getConnection(jdbcUrl, DATABASEUSER, DATABASEPASSWORD);
                LOG.info("Connected to Database");
                break;
            } catch (SQLException e) {
                retryCount++;
                LOG.error("Attempt {} - Could not connect to Database: {}", retryCount, e.getMessage());
                LOG.info("Retrying in {} seconds...", delaySeconds);
                    try {
                        Thread.sleep(Duration.ofSeconds(delaySeconds).toMillis());
                    } catch (InterruptedException ie) {
                        LOG.error("Retry sleep interrupted: {}", ie.getMessage());
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Failed to establish a database connection after " + retryCount + " attempts.");

                    }
            }
        }

        // setup rabbitmq connection
        exchangeName = new RabbitMqConfig().getExchange();
        bus = new BusConnector();
        bus.connect();


        stock = StockFactory.getStock();
        persistor = new MysqlDatabasePersistor(sqlConnection);
        orderManager = new OrderManager(this.stock,this.persistor, new LogMessageSender(bus,exchangeName,Routes.LOG));



        //read saved orders with items date < now()

        //try process orders
        receiveCentralWarehouseOrders();

        LOG.info("started receiving orders asynchronously");

    }

    private void receiveCentralWarehouseOrders() throws IOException{
        LOG.debug("Starting listening for messages with routing [{}]", Routes.WAREHOUSE_REGISTER);
        bus.listenFor(exchangeName,"CentralWarehouseService <- "+ Routes.WAREHOUSE_REGISTER,Routes.WAREHOUSE_REGISTER,new CentralWarehouseOrderReceiver(orderManager));
    }
    /**
     * @see AutoCloseable#close()
     */
    @Override
    public void close() {
        bus.close();
    }
}
