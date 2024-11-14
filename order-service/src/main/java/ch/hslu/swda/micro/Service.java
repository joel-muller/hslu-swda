package ch.hslu.swda.micro;

import ch.hslu.swda.messages.CustomerRequest;
import ch.hslu.swda.messages.LogMessage;
import ch.hslu.swda.messages.StoreRequest;
import ch.hslu.swda.messages.VerifyRequest;

import java.io.IOException;

public interface Service {
    public void checkValidity(VerifyRequest request) throws IOException, InterruptedException;
    public void log(LogMessage message) throws IOException, InterruptedException;
    public void requestArticlesFromStore(StoreRequest request) throws IOException, InterruptedException;
    public void checkCustomerValidity(CustomerRequest request) throws IOException, InterruptedException;
}
