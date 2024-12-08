package ch.hslu.swda.business;

import ch.hslu.swda.messagesOutgoing.*;
import ch.hslu.swda.micro.Service;

import java.io.IOException;

public class ServiceMock implements Service {
    LogMessage logMessage;
    VerifyRequest verifyRequest;
    StoreRequest storeRequest;
    CustomerRequest customerRequest;
    OrderReady orderReady;
    OrderCancelled orderCancelled;
    Invoice invoice;

    @Override
    public void checkValidity(VerifyRequest request) throws IOException {
        this.verifyRequest = request;
    }

    @Override
    public void log(LogMessage message) throws IOException {
        this.logMessage = message;

    }

    @Override
    public void requestArticlesFromStore(StoreRequest request) throws IOException {
        this.storeRequest = request;
    }

    @Override
    public void checkCustomerValidity(CustomerRequest request) throws IOException {
        this.customerRequest = request;
    }

    @Override
    public void sendOrderReadyToStore(OrderReady ready) throws IOException {
        this.orderReady = ready;
    }

    @Override
    public void sendOrderCancelledToStore(OrderCancelled cancelled) throws IOException {
        this.orderCancelled = cancelled;
    }

    @Override
    public void createInvoice(Invoice invoice) throws IOException {
        this.invoice = invoice;
    }
}
