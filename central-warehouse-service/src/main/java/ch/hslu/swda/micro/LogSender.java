package ch.hslu.swda.micro;

import ch.hslu.swda.messagesOutgoing.LogMessage;

import java.io.IOException;

public interface LogSender {

    void send(LogMessage logMessage)throws IOException;
}
