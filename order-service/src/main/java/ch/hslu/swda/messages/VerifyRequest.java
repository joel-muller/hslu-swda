package ch.hslu.swda.messages;

import java.util.*;

public class VerifyRequest {
    private final UUID orderId;
    private final UUID employeeId;
    private final Map<Integer, Integer> articles;

    public VerifyRequest(UUID orderID, Map<Integer, Integer> articles, UUID employeeId) {
        this.orderId = orderID;
        this.articles = articles;
        this.employeeId = employeeId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Map<Integer, Integer> getArticles() {
        return articles;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VerifyRequest that)) return false;
        return Objects.equals(orderId, that.orderId) && Objects.equals(articles, that.articles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, articles);
    }

    @Override
    public String toString() {
        return "VerifyRequest{" +
                "orderID=" + orderId +
                ", articles=" + articles +
                '}';
    }
}
