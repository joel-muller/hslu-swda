package ch.hslu.swda.messagesOutgoing;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record OrderConfirmation(UUID id, List<ArticleOrderConfirmationDTO> articles, Date date, UUID storeId, UUID customerId, UUID employeeId) {
}
