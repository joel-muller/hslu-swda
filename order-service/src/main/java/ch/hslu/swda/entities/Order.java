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
import java.util.Calendar;

import dev.morphia.annotations.*;

/**
 * Einfaches Datenmodell einer Bestellung.
 */

@Entity("order")
public final class Order {

    @Id
    private UUID id;
    private boolean valid;
    private Map<Integer, Integer> articles;
    private Date date;
    private UUID storeId;
    private UUID customerId;
    private UUID employeeId;

    public Order(Map<Integer, Integer> articles, UUID storeId, UUID customerId, UUID employeeId) {
        this.id = UUID.randomUUID();
        this.valid = false;
        this.articles = articles;
        this.date = Calendar.getInstance().getTime();
        this.storeId = storeId;
        this.customerId = customerId;
        this.employeeId = employeeId;
    }

    public static Order getExampleOrder() {
        Map<Integer, Integer> articles = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            articles.put(i, 5+i);
        }
        return new Order(articles,UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Map<Integer, Integer> getArticles() {
        return articles;
    }

    public void setArticles(Map<Integer, Integer> articles) {
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
                ", valid=" + valid +
                ", articles=" + articles +
                ", date=" + date +
                ", storeId=" + storeId +
                ", customerId=" + customerId +
                ", employeeId=" + employeeId +
                '}';
    }
}
