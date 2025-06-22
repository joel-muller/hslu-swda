package ch.hslu.swda.messages;

public record UserJWT(String jwt, boolean success) implements OutgoingMessage {
}
