package ch.hslu.swda.entities;

import java.util.UUID;

public record SimpleOrder(UUID orderId, UUID customerId) {
}
