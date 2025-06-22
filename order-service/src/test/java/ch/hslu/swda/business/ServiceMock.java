package ch.hslu.swda.business;

import ch.hslu.swda.messagesOutgoing.*;
import ch.hslu.swda.micro.Service;

import java.io.IOException;

public class ServiceMock implements Service {
    LogMessage logMessage;
    ArticleCheckValidity articleCheckValidity;
    StoreRequestArticles storeRequestArticles;
    CustomerValidate customerValidate;
    StoreOrderReady storeOrderReady;
    StoreOrderCancelled storeOrderCancelled;
    InvoiceCreate invoiceCreate;

    @Override
    public void checkValidity(ArticleCheckValidity request) throws IOException {
        this.articleCheckValidity = request;
    }

    @Override
    public void log(LogMessage message) throws IOException {
        this.logMessage = message;

    }

    @Override
    public void requestArticlesFromStore(StoreRequestArticles request) throws IOException {
        this.storeRequestArticles = request;
    }

    @Override
    public void checkCustomerValidity(CustomerValidate request) throws IOException {
        this.customerValidate = request;
    }

    @Override
    public void sendOrderReadyToStore(StoreOrderReady ready, InvoiceCreate invoiceCreate) throws IOException {
        this.storeOrderReady = ready;
        this.invoiceCreate = invoiceCreate;
    }

    @Override
    public void sendOrderCancelledToStore(StoreOrderCancelled cancelled) throws IOException {
        this.storeOrderCancelled = cancelled;
    }

}
