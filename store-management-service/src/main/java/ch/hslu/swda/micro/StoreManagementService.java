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
import ch.hslu.swda.business.DatabaseConnector;
import ch.hslu.swda.messages.LogMessage;
import ch.hslu.swda.messages.OutgoingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public final class StoreManagementService implements AutoCloseable, Service {

    private static final Logger LOG = LoggerFactory.getLogger(StoreManagementService.class);
    private final BusConnector bus;
    private final String exchangeName;
    private final DatabaseConnector database;

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

        this.database = new DatabaseConnector();

        this.receiveOrder();
        this.receiveStoreCreationRequest();
    }

    @Override
    public void log(LogMessage message) throws IOException {
        sendMessageAsynchronous(message, Routes.LOG);
    }

    public void sendMessageAsynchronous(OutgoingMessage message, String route) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(message);
        LOG.debug("Sending asynchronous message to broker with routing [{}]", Routes.LOG);
        bus.talkAsync(exchangeName, route, data);
    }

    private void receiveOrder() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.REQUEST_ARTICLES);
        bus.listenFor(exchangeName, "StoreManagementService <- " + Routes.REQUEST_ARTICLES, Routes.REQUEST_ARTICLES,
                new OrderReceiver(this.database, this));
    }

    /**
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws SQLException {
        bus.close();
    }

    private void receiveStoreCreationRequest() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.STORE_CREATION);
        bus.listenFor(exchangeName, "StoreManagementService <- " + Routes.STORE_CREATION, Routes.STORE_CREATION,
                new StoreCreationReciever(this.database, this));
    }
}
