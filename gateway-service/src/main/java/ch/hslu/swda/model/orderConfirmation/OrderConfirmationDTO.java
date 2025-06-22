package ch.hslu.swda.model.orderConfirmation;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

import java.util.Date;
import java.util.List;
import java.util.UUID;
@Introspected
@Serdeable
public record OrderConfirmationDTO(UUID id, List<ArticleOrderConfirmationDTO> articles, Date date, UUID storeId, UUID customerId, UUID employeeId, String totalPrice) {
}
