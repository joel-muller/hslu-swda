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
import ch.hslu.swda.entities.CentralWarehouseOrder;
import ch.hslu.swda.stock.api.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public final class CentralWarehouseOrderReceiver implements MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(CentralWarehouseOrderReceiver.class);
    private final String exchangeName;
    private final BusConnector bus;

    private final Stock stock;


    public CentralWarehouseOrderReceiver(final String exchangeName, final BusConnector bus,Stock stock) {
        this.exchangeName = exchangeName;
        this.bus = bus;
        this.stock = stock;
    }

    /**
     * @see MessageReceiver#onMessageReceived(String, String)
     */
    @Override
    public void onMessageReceived(final String route, final String message) {
        ObjectMapper mapper = new ObjectMapper();
        Map<Integer, Integer> articles;
        UUID storeId;
        CentralWarehouseOrder warehouseOrder;
        try {
            JsonNode orderNode = mapper.readTree(message);

            String articlesString = orderNode.get("articles").toString();
            articles= mapper.readValue(articlesString, new TypeReference<Map<Integer, Integer>>() {});
            storeId = UUID.fromString(orderNode.get("storeId").asText());

            warehouseOrder = new CentralWarehouseOrder(storeId,articles,stock);

        }catch (IOException|IllegalArgumentException  e){
            LOG.error(e.getMessage(), e);
            return;
        }
        LOG.info("warehouseOrder correctly received. Start processing Order");
        warehouseOrder.process();




    }
}
