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

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;
import ch.hslu.swda.entities.StoreManagementDB;

/**
 * Beispielcode für Implementation eines Servcies mit RabbitMQ.
 */
public final class StoreManagementService implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceTemplate.class);
    private final BusConnector bus;
    private final String exchangeName;
    private Connection connection;
    final StoreManagementDB db;



    /**
     * @throws IOException      IO-Fehler.
     * @throws TimeoutException Timeout.
     */
    public StoreManagementService() throws IOException, TimeoutException, SQLException {

        // thread info
        String threadName = Thread.currentThread().getName();
        LOG.debug("[Thread: {}] Service started", threadName);

        // setup rabbitmq connection
        this.exchangeName = new RabbitMqConfig().getExchange();
        this.bus = new BusConnector();
        this.bus.connect();
        this.db = new StoreManagementDB();


    }

    void provideArticleAvailability() throws SQLException {
        LOG.info("Checking article availability...");
        String queueName = "StoreManagementService <- " + Routes.INVENTORY_CHECK;
        try {
            bus.listenFor(exchangeName, queueName, Routes.INVENTORY_CHECK, new InventoryCheckReceiver(exchangeName, bus, db) );
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }

    }

    /**
     * Erzeugt eine Student-Entity und sendet einen Event.
     *
     * @throws IOException          IOException.
     * @throws InterruptedException InterruptedException.
     */
/*     public void registerStudent() throws IOException, InterruptedException {

        // create new student
        final Student student = new Student(1, "Joel", "Doe", ThreadLocalRandom.current().nextInt(1, 13));
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(student);

        // send message to register student in registry, sync communication (awaiting
        // response)
        LOG.debug("Sending asynchronous message to broker with routing [{}]", Routes.STUDENT_REGISTER);
        bus.talkAsync(exchangeName, Routes.STUDENT_REGISTER, data);

    } */

/*     public String askAboutUniverse() throws IOException, InterruptedException {

        // create question
        final String question = "What is the answer to the Ultimate Question of Life, the Universe, and Everything?";

        // send question to deep thought
        LOG.debug("Sending synchronous message to broker with routing [{}]", Routes.DEEP_THOUGHT_ASK);
        String response = bus.talkSync(exchangeName, Routes.DEEP_THOUGHT_ASK, question);

        // receive answer
        if (response == null) {
            LOG.debug("Received no response. Timeout occurred. Will retry later");
            return null;
        }
        LOG.debug("Received response to question \"{}\": {}", question, response);
        return response;
    } */

    /**
     * @throws IOException
     */
/*     private void receiveStatisticsChange() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.STATISTICS_CHANGED);
        bus.listenFor(exchangeName, "ServiceTemplate <- " + Routes.STATISTICS_CHANGED, Routes.STATISTICS_CHANGED,
                new StatsReceiver(exchangeName, bus));
    }

    private void receiveChatMessages() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.TEMPLATE_CHAT);
        bus.listenFor(exchangeName, "ServiceTemplate <- " + Routes.TEMPLATE_CHAT, Routes.TEMPLATE_CHAT,
                new ChatReceiver(exchangeName, bus));
    } */

    /**
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            LOG.info("Closed the MySQL database connection.");
        }
        bus.close();
    }
}
