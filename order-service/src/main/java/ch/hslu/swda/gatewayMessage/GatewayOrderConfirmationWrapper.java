package ch.hslu.swda.gatewayMessage;

public record GatewayOrderConfirmationWrapper(boolean valid, GatewayOrderConfirmation message) {
}
