package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.messagesOutgoing.LogMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LogMessageSender  implements LogSender{

    private final static Logger LOG =  LoggerFactory.getLogger(LogMessageSender.class);

    private final BusConnector bus;
    private final String exchangeName;

    private final String route;
    public LogMessageSender(BusConnector bus, String exchangeName,String route){
        this.bus = bus;
        this.exchangeName = exchangeName;
        this.route = route;
    }
    @Override
    public void send(LogMessage logMessage) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(logMessage);
        LOG.debug("Sending asynchronous message to broker with routing [{}]", route);
        bus.talkAsync(exchangeName, route, data);
    }
}
