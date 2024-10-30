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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public final class OrderReceiver implements MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(OrderReceiver.class);
    private final String exchangeName;
    private final BusConnector bus;

    public OrderReceiver(final String exchangeName, final BusConnector bus) {
        this.exchangeName = exchangeName;
        this.bus = bus;
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

            String articlesString = orderNode.get("articles").toString();
            Map<Integer, Integer> articles = mapper.readValue(articlesString, new TypeReference<Map<Integer, Integer>>() {});


            Order order = new Order(articles, orderNode.get("storeId").asInt(), orderNode.get("customerId").asInt(), orderNode.get("employeeId").asInt());

            LOG.info("Following order received: [{}]", order.toString());


            // Safe order to the database

            bus.reply(exchangeName, replyTo, corrId, "Order Successfully created: " + order.toString());
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

    }

}
