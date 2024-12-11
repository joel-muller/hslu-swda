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
import ch.hslu.swda.entities.Invoice;
import ch.hslu.swda.persistence.DatabaseConnector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;

/**
 * Beispielcode für Implementation eines Servcies mit RabbitMQ.
 */
public final class AccountingService implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(AccountingService.class);
    private final String exchangeName;
    private final BusConnector bus;
    private final DatabaseConnector database;

    /**
     * @throws IOException      IO-Fehler.
     * @throws TimeoutException Timeout.
     */
    AccountingService() throws IOException, TimeoutException {

        // thread info
        String threadName = Thread.currentThread().getName();
        LOG.debug("[Thread: {}] Service started", threadName);

        // setup rabbitmq connection
        this.exchangeName = new RabbitMqConfig().getExchange();
        this.bus = new BusConnector();
        this.bus.connect();
        this.database = new DatabaseConnector();

        this.initializeDummyData();
        // start message receivers
        LOG.info("Listening for messages...");
        this.createInvoices();
        this.receivePaymentStatusRequest();
        this.receiveInvoiceGetRequests();
        this.receiveInvoiceGetAllRequests();
    }

    /**
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() {
        bus.close();
    }

    private void createInvoices() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.INVOICE_CREATE);
        bus.listenFor(exchangeName, "AccountingService <- " + Routes.INVOICE_CREATE, Routes.INVOICE_CREATE,
                new InvoiceCreationReceiver(exchangeName, bus));
    }

    private void receivePaymentStatusRequest() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.PAYMENTSTATUS_GET);
        bus.listenFor(exchangeName, "AccountingService <- " + Routes.PAYMENTSTATUS_GET, Routes.PAYMENTSTATUS_GET,
                new PaymentStatusRequestReceiver(exchangeName, bus));
    }

    private void receiveInvoiceGetRequests() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.INVOICE_GET);
        bus.listenFor(exchangeName, "AccountingService <- " + Routes.INVOICE_GET, Routes.INVOICE_GET,
                new GetInvoiceReceiver(exchangeName, bus, this.database));
    }  

    private void receiveInvoiceGetAllRequests() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.INVOICES_GET);
        bus.listenFor(exchangeName, "AccountingService <- " + Routes.INVOICES_GET, Routes.INVOICES_GET,
                new GetAllInvoicesReceiver(exchangeName, bus, this.database));
    }  

    private void initializeDummyData() throws JsonProcessingException {
        // create some dummy data
        DatabaseConnector database = new DatabaseConnector();
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < 10; i++) {
            Invoice invoice = new Invoice(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                    new HashMap<>(), new HashMap<>(),
                    String.valueOf(ThreadLocalRandom.current().nextDouble(0, 10000.0)));
            database.storeInvoice(invoice);
            LOG.debug("Created dummy invoice: {}", mapper.writeValueAsString(invoice));
        }

    }

}
