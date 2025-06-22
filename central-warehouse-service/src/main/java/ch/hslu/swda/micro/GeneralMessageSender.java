package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;

import java.io.IOException;

public class GeneralMessageSender implements MessageSender{

    private final BusConnector busConnector;
    private final String exchangeName;

    GeneralMessageSender(BusConnector bus, String exchangeName){
        this.busConnector = bus;
        this.exchangeName = exchangeName;
    }
    @Override
    public void send(String message,String route) throws IOException {
        busConnector.talkAsync(exchangeName,route,message);
    }
}
