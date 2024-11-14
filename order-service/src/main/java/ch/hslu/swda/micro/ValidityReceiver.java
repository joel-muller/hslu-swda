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
import ch.hslu.swda.entities.ModifyContext;
import ch.hslu.swda.entities.ModifyValidity;
import ch.hslu.swda.entities.Order;
import ch.hslu.swda.messages.VerifyResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

public final class ValidityReceiver implements MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(ValidityReceiver.class);
    private final String exchangeName;
    private final BusConnector bus;
    private final DatabaseConnector database;


    public ValidityReceiver(final DatabaseConnector database, final String exchangeName, final BusConnector bus) {
        this.exchangeName = exchangeName;
        this.bus = bus;
        this.database = database;
    }

    /**
     * @see MessageReceiver#onMessageReceived(String, String, String, String)
     */
    @Override
    public void onMessageReceived(final String route, final String replyTo, final String corrId, final String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            VerifyResponse response = mapper.readValue(message, VerifyResponse.class);
            Order order = database.getById(response.idOrder());
            ModifyContext.modify(order, new ModifyValidity(response));
            LOG.info(order.toString());
        } catch (JsonProcessingException e) {
            LOG.error("Error occurred while mapping the validity reception data: {}", e.getMessage());
        }

        LOG.debug("received chat message with replyTo property [{}]: [{}]", replyTo, message);
        LOG.debug("sending answer with topic [{}] according to replyTo-property", replyTo);
        LOG.info("Received validity check: [{}]", message);
    }

}
