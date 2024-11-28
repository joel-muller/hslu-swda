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
import ch.hslu.swda.business.AuthDatabase;
import ch.hslu.swda.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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

        // setup db
        this.setupRightsAndRoles();

        // start message receivers
        this.receiveUsers();
        this.retrieveUsers();
        this.updateUsers();
        this.deleteUsers();
        this.authenticateUsers();
    }

    /**
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() {
        bus.close();
    }

    public void setupRightsAndRoles() {
        AuthDatabase db = new AuthDatabase();
        if (db.getAllRights().size() == 0 && db.getAllRoles().size() == 0) {
            db.setupStorage();
        }
    }

    private void receiveUsers() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.USER_CREATE);
        bus.listenFor(exchangeName, "AuthService <- " + Routes.USER_CREATE, Routes.USER_CREATE, new UserReceiver(exchangeName, bus));
    }

    private void retrieveUsers() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.USER_READ);
        bus.listenFor(exchangeName, "AuthService <- " + Routes.USER_READ, Routes.USER_READ, new UserRetriever(exchangeName, bus));
    }

    private void updateUsers() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.USER_UPDATE);
        bus.listenFor(exchangeName, "AuthService <- " + Routes.USER_UPDATE, Routes.USER_UPDATE, new UserUpdater(exchangeName, bus));
    }

    private void deleteUsers() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.USER_DELETE);
        bus.listenFor(exchangeName, "AuthService <- " + Routes.USER_DELETE, Routes.USER_DELETE, new UserDeleter(exchangeName, bus));
    }
    private void authenticateUsers() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", Routes.USER_LOGIN);
        bus.listenFor(exchangeName, "AuthService <- " + Routes.USER_LOGIN, Routes.USER_LOGIN, new UserAuthenticator(exchangeName, bus));
    }
}
