package ch.hslu.swda.entities;

import java.util.UUID;

public record ValidityRequest(UUID customerId, UUID employeeId) {
}
