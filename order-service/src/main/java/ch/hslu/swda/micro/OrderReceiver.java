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
import ch.hslu.swda.business.DatabaseConnector;
import ch.hslu.swda.entities.Article;
import ch.hslu.swda.entities.Order;
import ch.hslu.swda.entities.LogMessage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public final class OrderReceiver implements MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(OrderReceiver.class);
    private final String exchangeName;
    private final BusConnector bus;
    private final OrderService service;
    private final DatabaseConnector database;

    public OrderReceiver(final String exchangeName, final BusConnector bus, final OrderService service) {
        this.exchangeName = exchangeName;
        this.bus = bus;
        this.service = service;
        this.database = new DatabaseConnector();
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
            JsonNode orderNode = mapper.readTree(message);

            UUID orderId = UUID.randomUUID();

            String articlesString = orderNode.get("articles").toString();
            Map<Integer, Integer> articlesMap = mapper.readValue(articlesString, new TypeReference<Map<Integer, Integer>>() {});
            List<Article> articles = Article.createListArticle(articlesMap);
            for (Article art : articles) {
                this.database.storeArticle(art);
            }

            UUID storeId = UUID.fromString(orderNode.get("storeId").asText());
            UUID customerId = UUID.fromString(orderNode.get("customerId").asText());
            UUID employeeId = UUID.fromString(orderNode.get("employeeId").asText());

            Order order = new Order(orderId, articles, storeId, customerId, employeeId);

            this.database.storeOrder(order);

            LOG.info("Following order received and stored: [{}]", order.toString());
            service.log(new LogMessage(order.getEmployeeId(), "order.create", "Order Created: " + order.toString()));

            service.checkValidity(order);
            bus.reply(exchangeName, replyTo, corrId, "Order Successfully created: " + order.toString());
        } catch (IOException | InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }
    }

}
