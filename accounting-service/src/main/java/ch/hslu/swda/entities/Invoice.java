package ch.hslu.swda.entities;

import java.util.Map;
import java.util.UUID;

import ch.hslu.swda.IncomingMessages.InvoiceRequest;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

/**
 * Represents an invoice entity with details about the order, customer,
 * employee, store,
 * articles count, articles prices, and total price.
 */

@Entity("invoice")
public class Invoice {
    @Id
    private UUID Id;
    private UUID orderId;
    private UUID customerId;
    private UUID employeeId;
    private UUID storeId;
    private Map<Integer, Integer> articlesCount;
    private Map<Integer, String> articlesPrices;
    private String totalPrice;
    private Boolean PaymentOverdue;

    public Boolean getPaymentOverdue() {
        return PaymentOverdue;
    }

    public void setPaymentOverdue(Boolean paymentOverdue) {
        PaymentOverdue = paymentOverdue;
    }

    public Invoice() {
        // default constructor for Morphia to work
    }

    public Invoice(InvoiceRequest request) {
        this.Id = UUID.randomUUID();
        this.orderId = request.orderId();
        this.customerId = request.customerId();
        this.employeeId = request.employeeId();
        this.storeId = request.storeId();
        this.articlesCount = request.articlesCount();
        this.articlesPrices = request.articlesPrices();
        this.totalPrice = request.totalPrice();
        this.PaymentOverdue = false;
    }

    public Invoice(UUID orderId, UUID customerId, UUID employeeId, UUID storeId, Map<Integer, Integer> articlesCount,
            Map<Integer, String> articlesPrices, String totalPrice) {
        this.Id = UUID.randomUUID();
        this.orderId = orderId;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.storeId = storeId;
        this.articlesCount = articlesCount;
        this.articlesPrices = articlesPrices;
        this.totalPrice = totalPrice;
        this.PaymentOverdue = false;
    }

    public Invoice(UUID orderId, UUID customerId, UUID employeeId, UUID storeId, Map<Integer, Integer> articlesCount,
            Map<Integer, String> articlesPrices, String totalPrice, Boolean PaymentOverdue) {
        this.Id = UUID.randomUUID();
        this.orderId = orderId;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.storeId = storeId;
        this.articlesCount = articlesCount;
        this.articlesPrices = articlesPrices;
        this.totalPrice = totalPrice;
        this.PaymentOverdue = PaymentOverdue;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
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

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public Map<Integer, Integer> getArticlesCount() {
        return articlesCount;
    }

    public void setArticlesCount(Map<Integer, Integer> articlesCount) {
        this.articlesCount = articlesCount;
    }

    public Map<Integer, String> getArticlesPrices() {
        return articlesPrices;
    }

    public void setArticlesPrices(Map<Integer, String> articlesPrices) {
        this.articlesPrices = articlesPrices;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

}
