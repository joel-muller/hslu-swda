/*
 * Copyright 2024 Hochschule Luzern Informatik.
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
package ch.hslu.swda.bus;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.inject.Singleton;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logik um mit RabbitMQConnector zu kommunizieren.
 */
@Singleton
public class RabbitMQConnector {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQConnector.class);

    private String rabbitHost;
    private String rabbitUser;
    private String exchangeName;
    private Connection connection;
    private Channel channelTalk;
    private Channel channelListen;

    public RabbitMQConnector() {
        LOG.info("RabbitMQ-Objekt erzeugt.");
    }

    public void connect() {
        LOG.debug("Load configuration...");
        Dotenv env = Dotenv.load();
        this.rabbitHost = env.get("RABBITMQ_HOSTNAME");
        this.rabbitUser = env.get("RABBITMQ_USER");
        this.exchangeName = env.get("RABBITMQ_EXCHANGE");
        
        LOG.debug("Connect to RabbitMQ...");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(this.rabbitHost);
        factory.setUsername(this.rabbitUser);
        factory.setPassword(env.get("RABBITMQ_PASSWORD"));
        LOG.debug("Connecting to {} with user {}...", this.rabbitHost, this.rabbitUser);
        try {
            this.connection = factory.newConnection();
            this.channelTalk = connection.createChannel();
            this.channelListen = connection.createChannel();
            LOG.info("RabbitMQ: Successfully connected to {}...", this.rabbitHost);
        } catch (IOException | TimeoutException ex) {
            LOG.error("Verbindung zu RabbitMQ nicht erfolgreich: {}", ex.getMessage());
        }
    }

    /**
     * Beispiel für asynchrone Kommunikation (Send / Fire&Forget).
     *
     * @param route Route.
     * @param message Message.
     * @throws java.io.IOException IOException.
     */
    public void talkAsync(final String route, final String message) throws IOException {
        if (!this.isConnected()) {
            this.connect();
        }
        AMQP.BasicProperties props = new AMQP.BasicProperties();
        channelTalk.basicPublish(this.exchangeName, route, props, message.getBytes(StandardCharsets.UTF_8));
    }

    public void disconnect() {
        try {
            LOG.debug("Disconnecting from {}...", this.rabbitHost);
            if (Objects.nonNull(this.channelTalk)) {
                channelTalk.close();
            }
            if (Objects.nonNull(this.channelListen)) {
                channelListen.close();
            }
            if (Objects.nonNull(this.connection)) {
                connection.close();
                LOG.info("RabbitMQ: Successfully disconnected from {}...", this.rabbitHost);
            }
        } catch (IOException | TimeoutException ex) {
            LOG.error("Disconnect von RabbitMQ nicht erfolgreich: {}", ex.getMessage());
        }
    }

    public boolean isConnected() {
        return Objects.nonNull(this.connection) && this.connection.isOpen();
    }
}
