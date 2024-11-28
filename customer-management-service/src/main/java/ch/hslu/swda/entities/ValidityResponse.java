package ch.hslu.swda.entities;

import java.util.UUID;

public record ValidityResponse(UUID orderId, boolean exists) {
}
