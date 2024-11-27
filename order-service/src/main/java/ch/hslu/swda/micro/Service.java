package ch.hslu.swda.micro;

import ch.hslu.swda.messagesOutgoing.*;

import java.io.IOException;

public interface Service {
    void checkValidity(VerifyRequest request) throws IOException;
    void log(LogMessage message) throws IOException;
    void requestArticlesFromStore(StoreRequest request) throws IOException;
    void checkCustomerValidity(CustomerRequest request) throws IOException;
    void sendOrderReadyToStore(OrderReady ready) throws IOException;
}
