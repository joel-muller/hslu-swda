package ch.hslu.swda.gatewayMessage;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record GatewayOrderConfirmation(UUID id, List<GatewayOrderConfirmationArticle> articles, Date date, UUID storeId, UUID customerId, UUID employeeId, String totalPrice) {
}
