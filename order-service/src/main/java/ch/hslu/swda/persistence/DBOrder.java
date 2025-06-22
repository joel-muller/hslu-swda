package ch.hslu.swda.persistence;


import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.*;

@Entity("order")
public class DBOrder {
    @Id
    private UUID id;
    private List<DBArticle> articles;
    private Date date;
    private UUID storeId;
    private UUID customerId;
    private UUID employeeId;
    private int stateEnum;
    private boolean cancelled;

    public DBOrder(UUID id, List<DBArticle> articles, Date date, UUID storeId, UUID customerId, UUID employeeId, int stateEnum, boolean cancelled) {
        this.id = id;
        this.articles = articles;
        this.date = date;
        this.storeId = storeId;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.stateEnum = stateEnum;
        this.cancelled = cancelled;
    }

    public DBOrder() {
        this.id = UUID.randomUUID();
        this.articles = new ArrayList<>();
        this.date = Calendar.getInstance().getTime();
        this.storeId = UUID.randomUUID();
        this.customerId = UUID.randomUUID();
        this.employeeId = UUID.randomUUID();
        this.stateEnum = -1;
        this.cancelled = true;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getState() {
        return stateEnum;
    }

    public void setState(int state) {
        this.stateEnum = state;
    }

    public List<DBArticle> getArticles() {
        if (articles == null) {
            return new ArrayList<>();
        }
        return articles;
    }

    public void setArticles(List<DBArticle> articles) {
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


    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
