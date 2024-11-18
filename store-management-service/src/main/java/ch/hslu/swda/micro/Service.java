package ch.hslu.swda.micro;

import ch.hslu.swda.messages.LogMessage;

import java.io.IOException;

public interface Service {
    void log(LogMessage message) throws IOException;
}
