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

import java.util.*;

import ch.hslu.swda.messagesIngoing.VerifyResponse;
import ch.hslu.swda.messagesOutgoing.CustomerRequest;
import ch.hslu.swda.messagesOutgoing.StoreRequest;
import ch.hslu.swda.messagesOutgoing.VerifyRequest;

/**
 * Einfaches Datenmodell einer Bestellung.
 */

public final class Order {
    private final UUID id;
    private State state;
    private List<Article> articles;
    private final Date date;
    private final UUID storeId;
    private final UUID customerId;
    private final UUID employeeId;

    public Order(UUID id, Date date, UUID storeId, UUID customerId, UUID employeeId, State state, List<Article> articles) {
        this.id = id;
        this.date = date;
        this.storeId = storeId;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.state = state;
        this.articles = articles;
    }

    public UUID getId() {
        return id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public Date getDate() {
        return date;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public Price getTotalPrice() {
        int francs = 0;
        int centimes = 0;
        for (Article article : articles) {
            francs += article.getPrice().getFrancs();
            centimes += article.getPrice().getCentimes();
        }
        return new Price(francs, centimes);
    }

    public void setArticleInStore(int articleId) {
        for (Article article : articles) {
            if (article.getId() == articleId) {
                article.setDelivered(true);
                return;
            }
        }
    }

    public boolean allArticlesDelivered() {
        for (Article article : articles) {
            if (!article.isDelivered()) {
                return false;
            }
        }
        return true;
    }

    protected Map<Integer, Integer> createMapOfArticles() {
        Map<Integer, Integer> articles = new HashMap<Integer, Integer>();
        for (Article art : this.articles) {
            articles.put(art.getId(), art.getCount());
        }
        return articles;
    }


    public void handleVerifyResponse(VerifyResponse response) {
        if (!response.valid() || this.state.isCancelled()) {
            this.state.setCancelled(true);
            return;
        }
        Map<Integer, Integer> francsResponse = response.francsPerUnit();
        Map<Integer, Integer> centimesResponse = response.centimesPerUnit();
        for (Article article : this.articles) {
            int francs = francsResponse.getOrDefault(article.getId(), -1);
            int centimes = centimesResponse.getOrDefault(article.getId(), -1);
            if (francs < 0 || centimes < 0) {
                this.state.setCancelled(true);
                return;
            }
            article.setPrice(francs, centimes);
        }
        this.state.setValid(true);
    }

    public VerifyRequest getVerifyRequest() {
        return new VerifyRequest(getId(), createMapOfArticles(), getEmployeeId());
    }

    public StoreRequest getStoreRequest() {
        return new StoreRequest(getId(), createMapOfArticles(), getEmployeeId(), getStoreId());
    }

    public CustomerRequest getCustomerRequest() {
        return new CustomerRequest(getCustomerId(), getEmployeeId(), getId());
    }

    /**
     * Orders mit identischer ID sind gleich. {@inheritDoc}.
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        return obj instanceof Order ord
                && this.id == ord.id;
    }

    /**
     * Liefert Hashcode auf Basis der ID. {@inheritDoc}.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", state=" + state +
                ", articles=" + articles+
                ", date=" + date +
                ", storeId=" + storeId +
                ", customerId=" + customerId +
                ", employeeId=" + employeeId +
                '}';
    }
}
