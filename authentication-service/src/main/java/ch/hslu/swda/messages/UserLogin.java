package ch.hslu.swda.messages;

public record UserLogin(String username, String password) implements IngoingMessage{

}
