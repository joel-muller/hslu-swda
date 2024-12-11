package ch.hslu.swda.model.auth;

import java.util.UUID;

public record ClaimValidation(boolean success, UUID userId) {
}
