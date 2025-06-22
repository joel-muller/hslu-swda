package ch.hslu.swda.micro;

import ch.hslu.swda.messagesOutgoing.*;

import java.io.IOException;

public interface Service {
    void checkValidity(ArticleCheckValidity request) throws IOException;
    void log(LogMessage message) throws IOException;
    void requestArticlesFromStore(StoreRequestArticles request) throws IOException;
    void checkCustomerValidity(CustomerValidate request) throws IOException;
    void sendOrderReadyToStore(StoreOrderReady ready, InvoiceCreate invoiceCreate) throws IOException;
    void sendOrderCancelledToStore(StoreOrderCancelled cancelled) throws IOException;
}
