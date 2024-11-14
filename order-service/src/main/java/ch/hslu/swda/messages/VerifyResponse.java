package ch.hslu.swda.messages;

import java.util.UUID;

public record VerifyResponse(UUID idOrder, boolean valid) {}
