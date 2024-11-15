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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

import ch.hslu.swda.entities.LogMessage;
import ch.hslu.swda.entities.StoreManagementDB;

/**
 * The StoreManagementService class is responsible for managing the store's
 * inventory
 * and logging activities. It connects to a RabbitMQ message broker and a MySQL
 * database
 * to handle inventory updates and checks.
 * 
 * <p>
 * This class implements the AutoCloseable interface to ensure that resources
 * are
 * properly closed when the service is no longer needed.
 * </p>
 * 
 * <p>
 * Usage:
 * </p>
 * 
 * <pre>
 * {@code
 * try (StoreManagementService service = new StoreManagementService()) {
 *     service.provideArticleAvailability();
 *     service.updateArticleAvailability();
 *     // other operations
 * }
 * }
 * </pre>
 * 
 * <p>
 * Constructor:
 * </p>
 * <ul>
 * <li>{@link #StoreManagementService()} - Initializes the service, sets up
 * RabbitMQ and database connections.</li>
 * </ul>
 * 
 * <p>
 * Methods:
 * </p>
 * <ul>
 * <li>{@link #provideArticleAvailability()} - Listens for inventory check
 * messages and processes them.</li>
 * <li>{@link #updateArticleAvailability()} - Listens for inventory update
 * messages and processes them.</li>
 * <li>{@link #log(LogMessage)} - Sends a log message asynchronously to the
 * message broker.</li>
 * <li>{@link #close()} - Closes the database and message broker
 * connections.</li>
 * </ul>
 * 
 * <p>
 * Exceptions:
 * </p>
 * <ul>
 * <li>{@link java.io.IOException} - If an I/O error occurs.</li>
 * <li>{@link java.util.concurrent.TimeoutException} - If a timeout occurs.</li>
 * <li>{@link java.sql.SQLException} - If a database access error occurs.</li>
 * <li>{@link java.lang.InterruptedException} - If the logging operation is
 * interrupted.</li>
 * </ul>
 * 
 * @see java.lang.AutoCloseable
 */
public final class StoreManagementService implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(StoreManagementService.class);
    private final BusConnector bus;
    private final String exchangeName;
    // private Connection connection;
    final StoreManagementDB db;

    /**
     * @throws IOException      IO-Fehler.
     * @throws TimeoutException Timeout.
     */
    /**
     * Initializes a new instance of the StoreManagementService class.
     * 
     * @throws IOException      if an I/O error occurs during RabbitMQ connection
     *                          setup.
     * @throws TimeoutException if a timeout occurs during RabbitMQ connection
     *                          setup.
     * @throws SQLException     if a database access error occurs.
     */
    public StoreManagementService() throws IOException, TimeoutException, SQLException {

        // thread info
        String threadName = Thread.currentThread().getName();
        LOG.debug("[Thread: {}] Service started", threadName);

        // setup rabbitmq connection
        this.exchangeName = new RabbitMqConfig().getExchange();
        this.bus = new BusConnector();
        this.bus.connect();

        // setup database connection
        String url = "jdbc:mysql://localhost:3306/store_management";
        String user = "root";
        String password = "root";
        this.db = new StoreManagementDB(url, user, password);
    }

    /**
     * Listens for inventory check messages and processes them to provide article
     * availability.
     * 
     * This method sets up a listener for messages with a specific routing key
     * related to inventory checks.
     * It creates a queue named "StoreManagementService <- INVENTORY_CHECK" and uses
     * an
     * InventoryCheckReceiver to handle incoming messages.
     * 
     * @throws SQLException if a database access error occurs.
     */
    void provideArticleAvailability() throws SQLException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.INVENTORY_UPDATE);
        String queueName = "StoreManagementService <- " + Routes.INVENTORY_CHECK;
        try {
            bus.listenFor(exchangeName, queueName, Routes.INVENTORY_CHECK,
                    new InventoryCheckReceiver(exchangeName, bus, db));
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }

    }

    /**
     * Updates the availability of articles by listening for inventory update
     * messages.
     * 
     * This method sets up a listener for messages with a specific routing key
     * and processes them using an {@link InventoryUpdateReceiver}.
     * 
     * @throws SQLException if a database access error occurs
     */
    void updateArticleAvailability() throws SQLException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.INVENTORY_UPDATE);
        String queueName = "StoreManagementService <- " + Routes.INVENTORY_UPDATE;
        try {
            bus.listenFor(exchangeName, queueName, Routes.INVENTORY_UPDATE,
                    new InventoryUpdateReceiver(exchangeName, bus, db));
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }

    /*
     * Format für warehouse.order:
     * articles : Map <idArticle : Integer, count : Integer>
     * storeId: Integer
     * customerOrderId: Integer
     */

    private void orderFromCentralWarehouse() {
        try {
            db.viewInventoryTableAsJson().forEach(article -> {
                if (article.get("quantity").asInt() < article.get("minimumQuantity").asInt()) {
                    try {
                        //prepare a message as string with the following attributes: articles : Map <idArticle : Integer, count : Integer>, storeId: Integer, customerOrderId: Integer
                        ObjectMapper mapper = new ObjectMapper();
                        ObjectNode jsonNode = mapper.createObjectNode()
                                .putObject("articles")
                                .put(String.valueOf(article.get("article_id").asInt()), article.get("quantity").asInt())
                                .put("storeId", article.get("store").asInt())
                                .put("customerOrderId", article.get("customerOrderId").asInt());
                        String message = mapper.writeValueAsString(jsonNode);
                        bus.talkAsync(exchangeName, Routes.ORDER_FROM_CENTRAL_WAREHOUSE, message);
                    } catch (IOException e) {

                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } 
                    LOG.info("Ordering more of article: {} at store {}", article.get("article_id").asText(),
                            article.get("store").asText());
                }
            });
        } catch (SQLException e) {
            LOG.error("Error while checking article quantities: {}", e.getMessage());
        }
    }

    /**
     * Logs a message by serializing it to JSON and sending it asynchronously to a
     * message broker.
     *
     * @param message the log message to be sent
     * @throws IOException          if an I/O error occurs during message
     *                              serialization
     * @throws InterruptedException if the thread is interrupted while sending the
     *                              message
     */
    public void log(LogMessage message) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(message);

        LOG.debug("Sending asynchronous message to broker with routing [{}]", Routes.LOG);
        bus.talkAsync(exchangeName, Routes.LOG, data);
        LOG.debug("Create log");
    }

    /**
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws SQLException {
        db.close();
        bus.close();
    }
}
