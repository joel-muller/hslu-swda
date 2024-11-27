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

import java.io.IOException;
import java.util.*;
import java.util.Calendar;

import ch.hslu.swda.business.Modifiable;
import ch.hslu.swda.messages.CustomerRequest;
import ch.hslu.swda.messages.IngoingMessage;
import ch.hslu.swda.messages.StoreRequest;
import ch.hslu.swda.messages.VerifyRequest;
import ch.hslu.swda.micro.Service;
import dev.morphia.annotations.*;

/**
 * Einfaches Datenmodell einer Bestellung.
 */

@Entity("order")
public final class Order {
    @Id
    private UUID id;
    private State state;
    private List<Article> articles;
    private Date date;
    private UUID storeId;
    private UUID customerId;
    private UUID employeeId;

    public Order(UUID orderId, List<Article> articles, UUID storeId, UUID customerId, UUID employeeId) {
        this.id = orderId;
        this.state = new State();
        this.articles = articles;
        this.date = Calendar.getInstance().getTime();
        this.storeId = storeId;
        this.customerId = customerId;
        this.employeeId = employeeId;
    }

    public Order() {
        this.id = UUID.randomUUID();
        this.state = new State();
        this.articles = new ArrayList<>();
        this.date = Calendar.getInstance().getTime();
        this.storeId = UUID.randomUUID();
        this.customerId = UUID.randomUUID();
        this.employeeId = UUID.randomUUID();
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setState(State state) {
        this.state = state;
    }

    public UUID getId() {
        return id;
    }

    public State getState() {
        return state;
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

    public void setDate(Date date) {
        this.date = date;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
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

    public VerifyRequest getVerifyRequest() {
        return new VerifyRequest(getId(), createMapOfArticles(), getEmployeeId());
    }

    public StoreRequest getStoreRequest() {
        return new StoreRequest(getId(), createMapOfArticles(), getEmployeeId(), getStoreId());
    }

    public CustomerRequest getCustomerRequest() {
        return new CustomerRequest(getCustomerId(), getEmployeeId(), getId());
    }

    public void modify(Modifiable modifiable, IngoingMessage response, Service service) {
        modifiable.modify(this, response, service);
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
