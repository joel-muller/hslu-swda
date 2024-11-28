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
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.bus.RabbitMqConfig;
import ch.hslu.swda.persistence.DatabaseConnector;
import ch.hslu.swda.business.ModifyValidity;
import ch.hslu.swda.business.UpdateCustomer;
import ch.hslu.swda.business.UpdateOrder;
import ch.hslu.swda.messagesIngoing.*;
import ch.hslu.swda.messagesOutgoing.*;
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

        this.generalReceiver(Routes.RECEIVE_ORDER_VALIDITY, new Receiver<>(this.database, new ModifyValidity(), VerifyResponse.class, this));
        this.generalReceiver(Routes.RECEIVE_ORDER, new OrderReceiver(this.database, exchangeName, bus, this));
        this.generalReceiver(Routes.ORDER_UPDATE, new Receiver<>(this.database, new UpdateOrder(), OrderUpdate.class, this));
        this.generalReceiver(Routes.CUSTOMER_RECEIVE_VALIDITY, new Receiver<>(this.database, new UpdateCustomer(), CustomerResponse.class, this));
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

    @Override
    public void sendOrderReadyToStore(OrderReady ready) throws IOException {
        sendMessageAsynchronous(ready, Routes.ORDER_READY);
    }

    public void sendMessageAsynchronous(OutgoingMessage message, String route) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(message);
        LOG.debug("Sending asynchronous message to broker with routing [{}]", Routes.LOG);
        bus.talkAsync(exchangeName, route, data);
    }


    private void generalReceiver(String channel, MessageReceiver receiver) throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", channel);
        bus.listenFor(exchangeName, "OrderService <- " + channel, channel, receiver);
    }


    /**
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() {
        bus.close();
    }
}
