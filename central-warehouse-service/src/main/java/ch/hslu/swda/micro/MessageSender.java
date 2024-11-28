package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;

import java.io.IOException;

public interface MessageSender {

    void send(String message,String route) throws IOException;

}
