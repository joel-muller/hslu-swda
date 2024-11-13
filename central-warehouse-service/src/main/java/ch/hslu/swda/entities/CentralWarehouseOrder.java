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
package ch.hslu.swda.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


public final class CentralWarehouseOrder {

    @JsonProperty("id")
    private UUID id;
    @JsonProperty("storeId")
    private UUID storeId;
    @JsonProperty("articles")
    private List<OrderArticle> articles;


    private static final Logger LOG = LoggerFactory.getLogger(CentralWarehouseOrder.class);


    /**
     * Default Konstruktor.
     */
    public CentralWarehouseOrder(UUID storeId, Map<Integer,Integer> articles) throws IllegalArgumentException  {
        this.id = UUID.randomUUID();
        this.storeId = storeId;
        articles.forEach((k,v)->{
                this.articles.add(new OrderArticle(k,v));
            });
    }

    /**
     * @return the id
     */
    public UUID getId() {
        return this.id;
    }
    public List<OrderArticle> getArticles(){
        return this.articles;
    }

    /**
     * Orders mit identischer ID sind gleich. {@inheritDoc}.
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        return obj instanceof CentralWarehouseOrder warehouseOrder
                && this.id == warehouseOrder.id;
    }

    /**
     * Liefert Hashcode auf Basis der ID. {@inheritDoc}.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    /**
     * Liefert eine String-Repräsentation. {@inheritDoc}.
     */
    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage(), e);
            return "{}";
        }
    }
}
