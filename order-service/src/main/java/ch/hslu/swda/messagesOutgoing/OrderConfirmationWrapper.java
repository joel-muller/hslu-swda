package ch.hslu.swda.messagesOutgoing;

public record OrderConfirmationWrapper(boolean valid, OrderConfirmation message) {
}
