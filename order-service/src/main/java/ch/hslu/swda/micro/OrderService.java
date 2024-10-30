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
import ch.hslu.swda.entities.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;


/**
 * Beispielcode für Implementation eines Servcies mit RabbitMQ.
 */
public final class OrderService implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(OrderService.class);
    private final String exchangeName;
    private final BusConnector bus;

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

        // start message receivers
        this.receiveOrderValidity();
        this.receiveOrder();
    }

    public void checkValidity() throws IOException, InterruptedException {
        LOG.info("check validity here in the method");
        // create new student
        final Student student = new Student(1, "Jane", "Doe", ThreadLocalRandom.current().nextInt(1, 13));
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(student);

        // send message to register student in registry, sync communication (awaiting response)
        LOG.debug("Sending asynchronous message to broker with routing [{}]", Routes.CHECK_ORDER_VALIDITY);
        bus.talkAsync(exchangeName, Routes.CHECK_ORDER_VALIDITY, data);

    }

    private void receiveOrderValidity() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.RECEIVE_ORDER_VALIDITY);
        bus.listenFor(exchangeName, "OrderService <- " + Routes.RECEIVE_ORDER_VALIDITY, Routes.RECEIVE_ORDER_VALIDITY, new ValidityReceiver(exchangeName, bus));
    }


    private void receiveOrder() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.RECEIVE_ORDER);
        bus.listenFor(exchangeName, "OrderService <- " + Routes.RECEIVE_ORDER, Routes.RECEIVE_ORDER, new OrderReceiver(exchangeName, bus));

    }



    /**
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() {
        bus.close();
    }
}
