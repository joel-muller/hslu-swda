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

import ch.hslu.swda.entities.LogMessage;
import ch.hslu.swda.entities.Validity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public final class ValidityReceiver implements MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(ValidityReceiver.class);
    private final String exchangeName;
    private final BusConnector bus;
    private final ArticleRegistryService service;

    public ValidityReceiver(final String exchangeName, final BusConnector bus, ArticleRegistryService service) {
        this.exchangeName = exchangeName;
        this.bus = bus;
        this.service = service;
    }

    /**
     * @see MessageReceiver#onMessageReceived(String, String, String, String)
     */
    @Override
    public void onMessageReceived(final String route, final String replyTo, final String corrId, final String message) {

        // receive message and reply
        LOG.debug("received chat message with replyTo property [{}]: [{}]", replyTo, message);
        LOG.debug("sending answer with topic [{}] according to replyTo-property", replyTo);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(message);
            String articlesString = jsonNode.get("articles").toString();
            UUID orderId = UUID.fromString(jsonNode.get("id").asText());
            UUID employeeId = UUID.fromString(jsonNode.get("employeeId").asText());
            Map<Integer, Integer> articles = mapper.readValue(articlesString, new TypeReference<Map<Integer, Integer>>() {});
            LOG.info("Order with the id [{}] received articles: [{}]", orderId, articles);

            // TBD, Hier die artickel pruefen

            Validity validity = new Validity(true, orderId);

            service.log(new LogMessage(employeeId, "validity.ckecked", "Order validity of order " + validity.toString()));
            service.sendValidity(validity);
        } catch (IOException | InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }

    }

}
