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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;


/**
 * Beispielcode für Implementation eines Servcies mit RabbitMQ.
 */
public final class ServiceTemplate implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceTemplate.class);
    private final String exchangeName;
    private final BusConnector bus;

    /**
     * @throws IOException      IO-Fehler.
     * @throws TimeoutException Timeout.
     */
    ServiceTemplate() throws IOException, TimeoutException {

        // thread info
        String threadName = Thread.currentThread().getName();
        LOG.debug("[Thread: {}] Service started", threadName);

        // setup rabbitmq connection
        this.exchangeName = new RabbitMqConfig().getExchange();
        this.bus = new BusConnector();
        this.bus.connect();

        // start message receivers
        this.retrieveCustomers();
        this.receiveCustomers();
        this.validateCustomers();
        this.updateCustomers();
        this.deleteCustomers();
    }

    /**
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() {
        bus.close();
    }

    private void receiveCustomers() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.CUSTOMER_CREATE);
        bus.listenFor(exchangeName, "ServiceTemplate <- " + Routes.CUSTOMER_CREATE, Routes.CUSTOMER_CREATE, new CustomerReceiver(exchangeName, bus));
    }

    private void retrieveCustomers() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.CUSTOMER_GET);
        bus.listenFor(exchangeName, "ServiceTemplate <- " + Routes.CUSTOMER_GET, Routes.CUSTOMER_GET, new CustomerRetriever(exchangeName, bus));
    }

    private void validateCustomers() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.CUSTOMER_VALIDATE);
        bus.listenFor(exchangeName, "ServiceTemplate <- " + Routes.CUSTOMER_VALIDATE, Routes.CUSTOMER_VALIDATE, new CustomerValidator(exchangeName, bus));
    }

    private void updateCustomers() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.CUSTOMER_UPDATE);
        bus.listenFor(exchangeName, "ServiceTemplate <- " + Routes.CUSTOMER_UPDATE, Routes.CUSTOMER_UPDATE, new CustomerUpdater(exchangeName, bus));
    }

    private void deleteCustomers() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.CUSTOMER_DELETE);
        bus.listenFor(exchangeName, "ServiceTemplate <- " + Routes.CUSTOMER_DELETE, Routes.CUSTOMER_DELETE, new CustomerDeleter(exchangeName, bus));
    }
}
