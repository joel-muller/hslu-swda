package ch.hslu.swda.persistence;


import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.*;

@Entity("order")
public class DBOrder {
    @Id
    private UUID id;
    private DBState state;
    private List<DBArticle> articles;
    private Date date;
    private UUID storeId;
    private UUID customerId;
    private UUID employeeId;

    public DBOrder(UUID id, DBState state, List<DBArticle> articles, Date date, UUID storeId, UUID customerId, UUID employeeId) {
        this.id = id;
        this.state = state;
        this.articles = articles;
        this.date = date;
        this.storeId = storeId;
        this.customerId = customerId;
        this.employeeId = employeeId;
    }

    public DBOrder() {
        this.id = UUID.randomUUID();
        this.state = new DBState();
        this.articles = new ArrayList<>();
        this.date = Calendar.getInstance().getTime();
        this.storeId = UUID.randomUUID();
        this.customerId = UUID.randomUUID();
        this.employeeId = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public DBState getState() {
        return state;
    }

    public void setState(DBState state) {
        this.state = state;
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
}
