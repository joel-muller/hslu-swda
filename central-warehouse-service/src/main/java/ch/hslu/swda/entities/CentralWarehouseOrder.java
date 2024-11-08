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

import ch.hslu.swda.stock.api.Stock;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Einfaches Datenmodell eines Studenten.
 */
public final class CentralWarehouseOrder {

    @JsonProperty("id")
    private UUID id;
    @JsonProperty("storeId")
    private UUID storeId;
    @JsonProperty("articles")
    private Map<Integer,Integer> articles;

    private static final Logger LOG = LoggerFactory.getLogger(CentralWarehouseOrder.class);

    private Stock stock;

    /**
     * Default Konstruktor.
     */
    public CentralWarehouseOrder(UUID storeId, Map<Integer,Integer> articles, Stock stock) throws IllegalArgumentException  {
        this.id = UUID.randomUUID();
        this.storeId = storeId;
        validateArticles(articles);
        this.articles = articles;
        this.stock = stock;
    }



    /**
     * @return the id
     */
    public UUID getId() {
        return this.id;
    }

    /**
     * @param id the id to set
     */
    public void setId(final UUID id) {
        this.id = id;
    }

    /**
     * @return the firstName
     */
    public Map<Integer,Integer> getArticles() {
        return this.articles;
    }

    /**
     * @param articleId the id of the article
     * @param count the amount of articles of that id to add
     */
    public int addArticle(int articleId, int count) {
        if (this.articles.containsKey(articleId)){
            this.articles.put(articleId, this.articles.get(articleId)+count);
        }else{
            this.articles.put(articleId,count);
        }
        return this.articles.get(articleId);
    }

    /**
     * @param articles map to check for validity
     */
    public static void validateArticles(Map<Integer,Integer> articles) throws IllegalArgumentException{
        articles.forEach((k,v)->{
            if(v<1){
                throw new IllegalArgumentException("Cannot add article to order with count <1. Actual count: " +v+" for articleId "+k);
            }
            if(k<100000){
                throw new IllegalArgumentException("Cannot create order with articleId < 100'000 Actual articleID: " +k);
            }
        }
        );
        LOG.info("Verified warehouse articles for processing");
    }


    /**
     * @param articleId the id of the article
     * @param count the amount of articles of that id to remove
     * @return the amount of articles with articleId in warehouse order
     */
    public int removeArticle(int articleId, int count){
        if(this.articles.containsKey(articleId)){
            int currentCount = this.articles.get(articleId);
            int countAfter = currentCount-count;
            if(countAfter<=0){
                this.articles.remove(articleId);
                return 0;
            }
            this.articles.put(articleId,countAfter);
        }else{
            return 0;
        }


        return this.articles.get(articleId);
    }



    /**
     * Studenten mit identischer ID sind gleich. {@inheritDoc}.
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

    public void process(){
        this.articles.forEach((k,v)->{
            int ordered = stock.orderItem(k,v);
            String reserved = stock.reserveItem(k,v);
            if(reserved==null){
                LOG.info(("Could not reserve " + v+" items of articleId "+ k));
            }else{
                UUID wareHouseOrderItemReservationId = UUID.fromString(reserved);
                LOG.info("Reserved " + v+" items of articleId "+ k+". UUID: "+wareHouseOrderItemReservationId);
            }
            switch (ordered){
                case 0:
                    LOG.info("article "+ k +" not available in quantity " + v);
                    break;
                case -1:
                    LOG.error("invalid articleId: "+ k);
                    break;
                default:
                    LOG.info("ordered " +ordered +" of articleId "+ k);

            }
        });
    }

    /**
     * Liefert eine String-Repräsentation . {@inheritDoc}.
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
