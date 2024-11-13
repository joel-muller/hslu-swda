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

import ch.hslu.swda.persistence.CentralWarehouseOrderPersistor;
import ch.hslu.swda.persistence.DatabasePersistor;
import ch.hslu.swda.stock.api.Stock;
import ch.hslu.swda.stock.api.StockFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.plugins.jpeg.JPEGImageReadParam;
import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * Beispielcode für Implementation eines Servcies mit RabbitMQ.
 */
public final class CentralWarehouseService implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(CentralWarehouseService.class);
    private final String exchangeName;
    private final BusConnector bus;
    private final Stock stock;
    private final DatabasePersistor persistor;

    private final CentralWarehouseOrderManager orderManager;


    /**
     * @throws IOException      IO-Fehler.
     * @throws TimeoutException Timeout.
     */
    CentralWarehouseService() throws IOException, TimeoutException {
        // thread info
        String threadName = Thread.currentThread().getName();
        LOG.debug("[Thread: {}] Service started", threadName);

        // setup rabbitmq connection
        this.exchangeName = new RabbitMqConfig().getExchange();
        this.bus = new BusConnector();
        this.bus.connect();
        this.stock = StockFactory.getStock();
        this.persistor = new DatabasePersistor();
        this.orderManager = new OrderManager(this.stock,this.persistor);


        //read saved orders with items date < now()

        //try process orders
        this.receiveCentralWarehouseOrders();
    }

    private void receiveCentralWarehouseOrders() throws IOException{
        LOG.debug("Starting listening for messages with routing [{}]", Routes.WAREHOUSE_REGISTER);
        bus.listenFor(exchangeName,"CentralWarehouseService <- "+ Routes.WAREHOUSE_REGISTER,Routes.WAREHOUSE_REGISTER,new CentralWarehouseOrderReceiver(exchangeName,bus, orderManager));

    }
    /**
     * @see AutoCloseable#close()
     */
    @Override
    public void close() {
        bus.close();
    }
}
