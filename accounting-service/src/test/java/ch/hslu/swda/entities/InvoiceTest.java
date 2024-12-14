package ch.hslu.swda.entities;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import ch.hslu.swda.IncomingMessages.InvoiceRequest;

public class InvoiceTest {

    @Test
    public void testDefaultConstructor() {
        Invoice invoice = new Invoice();
        assertThat(invoice).isNotNull();
    }

    @Test
    public void testConstructorWithInvoiceRequest() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        Map<Integer, Integer> articlesCount = new HashMap<>();
        Map<Integer, String> articlesPrices = new HashMap<>();
        String totalPrice = "100.00";

        InvoiceRequest request = new InvoiceRequest(orderId, customerId, employeeId, storeId, articlesCount,
                articlesPrices, totalPrice);
        Invoice invoice = new Invoice(request);

        assertThat(invoice.getOrderId()).isEqualTo(orderId);
        assertThat(invoice.getCustomerId()).isEqualTo(customerId);
        assertThat(invoice.getEmployeeId()).isEqualTo(employeeId);
        assertThat(invoice.getStoreId()).isEqualTo(storeId);
        assertThat(invoice.getArticlesCount()).isEqualTo(articlesCount);
        assertThat(invoice.getArticlesPrices()).isEqualTo(articlesPrices);
        assertThat(invoice.getTotalPrice()).isEqualTo(totalPrice);
        assertThat(invoice.getPaymentOverdue()).isFalse();
    }

    @Test
    public void testConstructorWithParameters() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        Map<Integer, Integer> articlesCount = new HashMap<>();
        Map<Integer, String> articlesPrices = new HashMap<>();
        String totalPrice = "100.00";

        Invoice invoice = new Invoice(orderId, customerId, employeeId, storeId, articlesCount, articlesPrices,
                totalPrice);

        assertThat(invoice.getOrderId()).isEqualTo(orderId);
        assertThat(invoice.getCustomerId()).isEqualTo(customerId);
        assertThat(invoice.getEmployeeId()).isEqualTo(employeeId);
        assertThat(invoice.getStoreId()).isEqualTo(storeId);
        assertThat(invoice.getArticlesCount()).isEqualTo(articlesCount);
        assertThat(invoice.getArticlesPrices()).isEqualTo(articlesPrices);
        assertThat(invoice.getTotalPrice()).isEqualTo(totalPrice);
        assertThat(invoice.getPaymentOverdue()).isFalse();
    }

    @Test
    public void testConstructorWithAllParameters() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        Map<Integer, Integer> articlesCount = new HashMap<>();
        Map<Integer, String> articlesPrices = new HashMap<>();
        String totalPrice = "100.00";
        Boolean paymentOverdue = true;

        Invoice invoice = new Invoice(orderId, customerId, employeeId, storeId, articlesCount, articlesPrices,
                totalPrice, paymentOverdue);

        assertThat(invoice.getOrderId()).isEqualTo(orderId);
        assertThat(invoice.getCustomerId()).isEqualTo(customerId);
        assertThat(invoice.getEmployeeId()).isEqualTo(employeeId);
        assertThat(invoice.getStoreId()).isEqualTo(storeId);
        assertThat(invoice.getArticlesCount()).isEqualTo(articlesCount);
        assertThat(invoice.getArticlesPrices()).isEqualTo(articlesPrices);
        assertThat(invoice.getTotalPrice()).isEqualTo(totalPrice);
        assertThat(invoice.getPaymentOverdue()).isEqualTo(paymentOverdue);
    }

    @Test
    public void testSettersAndGetters() {
        Invoice invoice = new Invoice();

        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        Map<Integer, Integer> articlesCount = new HashMap<>();
        Map<Integer, String> articlesPrices = new HashMap<>();
        String totalPrice = "100.00";
        Boolean paymentOverdue = true;

        invoice.setOrderId(orderId);
        invoice.setCustomerId(customerId);
        invoice.setEmployeeId(employeeId);
        invoice.setStoreId(storeId);
        invoice.setArticlesCount(articlesCount);
        invoice.setArticlesPrices(articlesPrices);
        invoice.setTotalPrice(totalPrice);
        invoice.setPaymentOverdue(paymentOverdue);

        assertThat(invoice.getOrderId()).isEqualTo(orderId);
        assertThat(invoice.getCustomerId()).isEqualTo(customerId);
        assertThat(invoice.getEmployeeId()).isEqualTo(employeeId);
        assertThat(invoice.getStoreId()).isEqualTo(storeId);
        assertThat(invoice.getArticlesCount()).isEqualTo(articlesCount);
        assertThat(invoice.getArticlesPrices()).isEqualTo(articlesPrices);
        assertThat(invoice.getTotalPrice()).isEqualTo(totalPrice);
        assertThat(invoice.getPaymentOverdue()).isEqualTo(paymentOverdue);
    }
}