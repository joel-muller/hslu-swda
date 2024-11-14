package ch.hslu.swda.business;

import ch.hslu.swda.messages.CustomerRequest;
import ch.hslu.swda.messages.LogMessage;
import ch.hslu.swda.messages.StoreRequest;
import ch.hslu.swda.messages.VerifyRequest;
import ch.hslu.swda.micro.Service;

import java.io.IOException;

public class ServiceMock implements Service {
    @Override
    public void checkValidity(VerifyRequest request) throws IOException {

    }

    @Override
    public void log(LogMessage message) throws IOException {

    }

    @Override
    public void requestArticlesFromStore(StoreRequest request) throws IOException {

    }

    @Override
    public void checkCustomerValidity(CustomerRequest request) throws IOException {

    }
}
