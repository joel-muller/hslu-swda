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
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.MidiMessage;
import java.util.*;

public final class CentralWarehouseOrder {

    @JsonProperty("id")
    private UUID id;
    @JsonProperty("storeId")
    private UUID storeId;
    @JsonProperty("articles")
    private List<OrderArticle> articles;

    @JsonProperty("customerOrderId")
    private UUID customerOrderId;
    @JsonProperty("cancelled")
    private boolean cancelled;

    private static final Logger LOG = LoggerFactory.getLogger(CentralWarehouseOrder.class);


    /**
     * Default Konstruktor.
     */
    public CentralWarehouseOrder(UUID storeId, Map<Integer,Integer> articles) throws IllegalArgumentException  {
        this.id = UUID.randomUUID();
        this.storeId = storeId;
        this.articles= new ArrayList<>();

        articles.forEach((k,v)->{
                this.articles.add(new OrderArticle(k,v));
            });
    }

    public CentralWarehouseOrder(UUID storeId, Map<Integer,Integer> articles, UUID customerOrderId) throws IllegalArgumentException  {
        this.id = UUID.randomUUID();
        this.storeId = storeId;
        this.articles= new ArrayList<>();
        articles.forEach((k,v)->{
            this.articles.add(new OrderArticle(k,v));
        });
        this.customerOrderId = customerOrderId;
    }

    public CentralWarehouseOrder(UUID id, UUID storeId, UUID customerOrderId, boolean cancelled, List<OrderArticle> articles){
        this.id = id;
        this.storeId = storeId;
        this.customerOrderId = customerOrderId;
        this.cancelled = cancelled;
        this.articles = articles;
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
    public OrderArticle getArticleById(int articleId){
        for (OrderArticle article : articles) {
            if (article.getId() == articleId) {
                return article;
            }
        }
        return null; // Return null if no article matches
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

    public UUID getStoreId() {
        return this.storeId;
    }
    public UUID getCustomerOrderId(){
        return this.customerOrderId;
    }

    public void cancelOrder(){
        this.cancelled = true;
    }

    public boolean getCancelled(){
        return this.cancelled;
    }

    public boolean isComplete() {
        for(OrderArticle article: articles){
            if (article.getFulfilled()<article.getCount()) return false;
        }
        return true;
    }
}
