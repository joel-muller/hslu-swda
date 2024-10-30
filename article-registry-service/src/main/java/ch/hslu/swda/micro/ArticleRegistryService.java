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
import ch.hslu.swda.entities.LogMessage;
import ch.hslu.swda.entities.Student;
import ch.hslu.swda.entities.Validity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;


/**
 * Beispielcode für Implementation eines Servcies mit RabbitMQ.
 */
public final class ArticleRegistryService implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleRegistryService.class);
    private final String exchangeName;
    private final BusConnector bus;

    /**
     * @throws IOException      IO-Fehler.
     * @throws TimeoutException Timeout.
     */
    ArticleRegistryService() throws IOException, TimeoutException {

        // thread info
        String threadName = Thread.currentThread().getName();
        LOG.debug("[Thread: {}] Service started", threadName);

        // setup rabbitmq connection
        this.exchangeName = new RabbitMqConfig().getExchange();
        this.bus = new BusConnector();
        this.bus.connect();

        // start message receivers
        this.receiveValidityCheck();
    }

    public void sendValidity(Validity validity) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(validity);

        LOG.debug("Sending asynchronous message to broker with routing [{}]", Routes.RECEIVE_ORDER_VALIDITY);
        bus.talkAsync(exchangeName, Routes.RECEIVE_ORDER_VALIDITY, data);
    }

    public void log(LogMessage message) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(message);

        LOG.debug("Sending asynchronous message to broker with routing [{}]", Routes.LOG);
        bus.talkAsync(exchangeName, Routes.LOG, data);

        LOG.debug("Create log");
    }



    private void receiveValidityCheck() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.CHECK_ORDER_VALIDITY);
        bus.listenFor(exchangeName, "ArticleRegistry <- " + Routes.CHECK_ORDER_VALIDITY, Routes.CHECK_ORDER_VALIDITY, new ValidityReceiver(exchangeName, bus, this));
    }

    /**
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() {
        bus.close();
    }
}
