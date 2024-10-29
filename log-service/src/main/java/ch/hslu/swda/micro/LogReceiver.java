package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogReceiver implements MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(LogReceiver.class);
    private final String exchangeName;
    private final BusConnector bus;

    public LogReceiver(final String exchangeName, final BusConnector bus) {
        this.exchangeName = exchangeName;
        this.bus = bus;
    }

    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {

    }
}
