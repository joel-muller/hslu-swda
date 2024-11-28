package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.messagesOutgoing.LogMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LogMessageSender  implements LogSender{

    private final static Logger LOG =  LoggerFactory.getLogger(LogMessageSender.class);
    private final String route;

    private final MessageSender messageSender;

    public LogMessageSender(MessageSender messageSender, String route){
        this.messageSender = messageSender;
        this.route = route;
    }
    @Override
    public void send(LogMessage logMessage) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(logMessage);
        LOG.debug("Sending asynchronous message to broker with routing [{}]", route);
        messageSender.send(data, route);
    }
}
