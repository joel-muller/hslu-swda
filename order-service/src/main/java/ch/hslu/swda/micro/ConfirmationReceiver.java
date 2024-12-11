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
import ch.hslu.swda.entities.Order;
import ch.hslu.swda.messagesIngoing.CreateOrder;
import ch.hslu.swda.messagesIngoing.OrderConfirmationRequest;
import ch.hslu.swda.messagesOutgoing.LogMessage;
import ch.hslu.swda.persistence.DatabaseConnector;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public final class ConfirmationReceiver implements MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(ConfirmationReceiver.class);
    private final String exchangeName;
    private final BusConnector bus;
    private final OrderService service;
    private final DatabaseConnector database;

    public ConfirmationReceiver(final DatabaseConnector database, final String exchangeName, final BusConnector bus, final OrderService service) {
        this.exchangeName = exchangeName;
        this.bus = bus;
        this.service = service;
        this.database = database;
    }

    /**
     * @see MessageReceiver#onMessageReceived(String, String, String, String)
     */
    @Override
    public void onMessageReceived(final String route, final String replyTo, final String corrId, final String message) {

        // receive message and reply
        try {
            LOG.debug("received chat message with replyTo property [{}]: [{}]", replyTo, message);
            LOG.debug("sending answer with topic [{}] according to replyTo-property", replyTo);

            ObjectMapper mapper = new ObjectMapper();
            OrderConfirmationRequest orderNode = mapper.readValue(message, OrderConfirmationRequest.class);

            Order order = database.getById(orderNode.getOrderId());

            if (order == null) {
                bus.reply(exchangeName, replyTo, corrId, "null");
            } else {
                bus.reply(exchangeName, replyTo, corrId, mapper.writeValueAsString(order.getOrderConfirmation()));
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

}
