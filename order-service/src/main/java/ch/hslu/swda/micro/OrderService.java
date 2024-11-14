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
import ch.hslu.swda.messages.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * Beispielcode für Implementation eines Servcies mit RabbitMQ.
 */
public final class OrderService implements AutoCloseable, Service {

    private static final Logger LOG = LoggerFactory.getLogger(OrderService.class);
    private final String exchangeName;
    private final BusConnector bus;
    private final DatabaseConnector database;

    /**
     * @throws IOException      IO-Fehler.
     * @throws TimeoutException Timeout.
     */
    OrderService() throws IOException, TimeoutException {

        // thread info
        String threadName = Thread.currentThread().getName();
        LOG.debug("[Thread: {}] Service started", threadName);

        // setup rabbitmq connection
        this.exchangeName = new RabbitMqConfig().getExchange();
        this.bus = new BusConnector();
        this.bus.connect();

        // setup database
        this.database = new DatabaseConnector();

        // start message receivers
        this.receiveOrderValidity();
        this.receiveOrder();
    }

    @Override
    public void checkValidity(VerifyRequest request) throws IOException {
        sendMessageAsynchronous(request, Routes.CHECK_ORDER_VALIDITY);
    }

    @Override
    public void log(LogMessage message) throws IOException {
        sendMessageAsynchronous(message, Routes.LOG);
    }

    @Override
    public void requestArticlesFromStore(StoreRequest request) throws IOException {
        sendMessageAsynchronous(request, Routes.REQUEST_ARTICLES);
    }

    @Override
    public void checkCustomerValidity(CustomerRequest request) throws IOException {
        sendMessageAsynchronous(request, Routes.CHECK_CUSTOMER);
    }

    public void sendMessageAsynchronous(OutgoingMessage message, String route) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(message);
        LOG.debug("Sending asynchronous message to broker with routing [{}]", Routes.LOG);
        bus.talkAsync(exchangeName, route, data);
    }

    private void receiveOrderValidity() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.RECEIVE_ORDER_VALIDITY);
        bus.listenFor(exchangeName, "OrderService <- " + Routes.RECEIVE_ORDER_VALIDITY, Routes.RECEIVE_ORDER_VALIDITY, new ValidityReceiver(this.database, this));
    }


    private void receiveOrder() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.RECEIVE_ORDER);
        bus.listenFor(exchangeName, "OrderService <- " + Routes.RECEIVE_ORDER, Routes.RECEIVE_ORDER, new OrderReceiver(this.database, exchangeName, bus, this));
    }



    /**
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() {
        bus.close();
    }
}
