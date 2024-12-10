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
import ch.hslu.swda.business.ArticleHandler;
import ch.hslu.swda.entities.LogMessage;
import ch.hslu.swda.entities.Validity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public final class BookRequestReceiver implements MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(BookRequestReceiver.class);
    private final ArticleRegistryService service;
    private final ObjectMapper mapper = new ObjectMapper();

    public BookRequestReceiver(ArticleRegistryService service) {
        this.service = service;
    }

    /**
     * @see MessageReceiver#onMessageReceived(String, String, String, String)
     */
    @Override
    public void onMessageReceived(final String route, final String replyTo, final String corrId, final String message) {

        // receive message and reply
        LOG.info("received chat message with replyTo property [{}]: [{}]", replyTo, message);

            LOG.debug("sending answer with topic [{}] according to replyTo-property", replyTo);

            try{
                JsonNode root= mapper.readTree(message);

                int page = root.get("page").asInt(0);
                int size = root.get("size").asInt(100);

                // Validate pagination parameters
                if (page < 0) {
                    LOG.error("Page parameter must be a non-negative integer.");
                    return;
                }
                if (size < 1) {
                    LOG.error("Size parameter must be at least 1.");
                    return;
                }
                service.sendBooks(replyTo,corrId, page, size);

            }catch (JsonProcessingException e){
                LOG.error(e.getMessage());
            }

    }

}
