package ch.hslu.swda.micro;

import ch.hslu.swda.messages.CustomerRequest;
import ch.hslu.swda.messages.LogMessage;
import ch.hslu.swda.messages.StoreRequest;
import ch.hslu.swda.messages.VerifyRequest;

import java.io.IOException;

public interface Service {
    void checkValidity(VerifyRequest request) throws IOException;
    void log(LogMessage message) throws IOException;
    void requestArticlesFromStore(StoreRequest request) throws IOException;
    void checkCustomerValidity(CustomerRequest request) throws IOException;
}
