package ch.hslu.swda.messages;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class StoreRequest implements OutgoingMessage {
    private final UUID orderId;
    private final UUID employeeId;
    private final UUID storeId;
    private final Map<Integer, Integer> articles;

    public StoreRequest(UUID orderID, Map<Integer, Integer> articles, UUID employeeId, UUID storeId) {
        this.orderId = orderID;
        this.articles = articles;
        this.employeeId = employeeId;
        this.storeId = storeId;
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
        if (!(o instanceof StoreRequest that)) return false;
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
