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

import ch.hslu.swda.messagesIngoing.CreateOrder;
import ch.hslu.swda.messagesIngoing.OrderUpdate;
import ch.hslu.swda.messagesIngoing.VerifyResponse;
import ch.hslu.swda.messagesOutgoing.*;

/**
 * Einfaches Datenmodell einer Bestellung.
 */

public final class Order {
    private final UUID id;
    private final List<Article> articles;
    private final Date date;
    private final UUID storeId;
    private final UUID customerId;
    private final UUID employeeId;
    private StateEnum stateEnum;
    private boolean cancelled;

    public Order(UUID id, Date date, UUID storeId, UUID customerId, UUID employeeId, List<Article> articles, StateEnum stateEnum, boolean cancelled) {
        this.id = id;
        this.date = date;
        this.storeId = storeId;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.articles = Article.copyListArticle(articles);
        this.cancelled = cancelled;
        this.stateEnum = stateEnum;
    }

    public Order(CreateOrder createOrder) {
        this(UUID.randomUUID(), Calendar.getInstance().getTime(), createOrder.storeId(), createOrder.customerId(), createOrder.employeeId(), Article.createListArticle(createOrder.articles()), StateEnum.STORED, false);
    }

    public UUID getId() {
        return id;
    }

    public StateEnum getState() {
        return stateEnum;
    }

    public List<Article> getCopyOfArticles() {
        List<Article> copy = new ArrayList<>();
        for (Article article : this.articles) {
            copy.add(article.getCopy());
        }
        return copy;
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

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled() {
        cancelled = true;
    }

    public boolean isArticlesValid() {
        return !(this.stateEnum.equals(StateEnum.STORED));
    }

    public void setArticlesValid() {
        if (this.stateEnum.equals(StateEnum.STORED)) {
            this.stateEnum = StateEnum.ARTICLE_VALIDATED;
        }
    }

    public void setCustomerValid() {
        if (this.stateEnum.equals(StateEnum.ARTICLE_VALIDATED)) {
            this.stateEnum = StateEnum.CUSTOMER_VALID;
            if (allArticlesDelivered()) {
                this.stateEnum = StateEnum.READY;
            }
        }
    }

    public boolean isCustomerReady() {
        return (this.stateEnum.equals(StateEnum.CUSTOMER_VALID) || this.stateEnum.equals(StateEnum.READY));
    }

    public boolean isReady() {
        return this.stateEnum.equals(StateEnum.READY);
    }

    public void tryToSetReady() {
        if (this.stateEnum.equals(StateEnum.CUSTOMER_VALID) && allArticlesDelivered()) {
            this.stateEnum = StateEnum.READY;
        }
    }

    public boolean isArticleReady() {
        for (Article article : articles) {
            if (!article.isDelivered()) {
                return false;
            }
        }
        return true;
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

    public Map<Integer, Integer> createMapOfArticles() {
        Map<Integer, Integer> articles = new HashMap<Integer, Integer>();
        for (Article art : this.articles) {
            articles.put(art.getId(), art.getCount());
        }
        return articles;
    }


    public void handleVerifyResponse(VerifyResponse response) {
        if (!response.valid()) {
            setCancelled();
            return;
        }
        Map<Integer, Integer> francsResponse = response.francsPerUnit();
        Map<Integer, Integer> centimesResponse = response.centimesPerUnit();
        for (Article article : this.articles) {
            int francs = francsResponse.getOrDefault(article.getId(), -1);
            int centimes = centimesResponse.getOrDefault(article.getId(), -1);
            if (francs < 0 || centimes < 0) {
                setCancelled();
                return;
            }
            article.setPrice(francs, centimes);
        }
        setArticlesValid();
    }

    public void handleOrderUpdate(OrderUpdate update) {
        if (!update.storeValid()) {
            setCancelled();
            return;
        }
        List<Integer> readyOrders = update.articles();
        for (int article : readyOrders) {
            setArticleInStore(article);
        }
        tryToSetReady();
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

    public OrderReady getOrderReady() {
        return new OrderReady(getId(), getStoreId());
    }

    public OrderCancelled getOrderCancelled() {
        return new OrderCancelled(getId(), getStoreId());
    }

    /**
     * Orders mit identischer ID sind gleich. {@inheritDoc}.
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Order order)) return false;
        return Objects.equals(order.getId(), this.getId());
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
                ", state=" + stateEnum +
                ", articles=" + articles+
                ", date=" + date +
                ", storeId=" + storeId +
                ", customerId=" + customerId +
                ", employeeId=" + employeeId +
                '}';
    }
}
