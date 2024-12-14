package ch.hslu.swda.IncomingMessages;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class InvoiceRequestTest {

    @Test
    public void testInvoiceRequestCreation() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        Map<Integer, Integer> articlesCount = Map.of(1, 2, 2, 3);
        Map<Integer, String> articlesPrices = Map.of(1, "15.45", 2, "30.90");
        String totalPrice = "46.35";

        InvoiceRequest invoiceRequest = new InvoiceRequest(orderId, customerId, employeeId, storeId, articlesCount,
                articlesPrices, totalPrice);

        assertThat(invoiceRequest.orderId()).isEqualTo(orderId);
        assertThat(invoiceRequest.customerId()).isEqualTo(customerId);
        assertThat(invoiceRequest.employeeId()).isEqualTo(employeeId);
        assertThat(invoiceRequest.storeId()).isEqualTo(storeId);
        assertThat(invoiceRequest.articlesCount()).isEqualTo(articlesCount);
        assertThat(invoiceRequest.articlesPrices()).isEqualTo(articlesPrices);
        assertThat(invoiceRequest.totalPrice()).isEqualTo(totalPrice);
    }

    @Test
    public void testInvoiceRequestWithEmptyMaps() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        Map<Integer, Integer> articlesCount = Map.of();
        Map<Integer, String> articlesPrices = Map.of();
        String totalPrice = "0.00";

        InvoiceRequest invoiceRequest = new InvoiceRequest(orderId, customerId, employeeId, storeId, articlesCount,
                articlesPrices, totalPrice);

        assertThat(invoiceRequest.orderId()).isEqualTo(orderId);
        assertThat(invoiceRequest.customerId()).isEqualTo(customerId);
        assertThat(invoiceRequest.employeeId()).isEqualTo(employeeId);
        assertThat(invoiceRequest.storeId()).isEqualTo(storeId);
        assertThat(invoiceRequest.articlesCount()).isEqualTo(articlesCount);
        assertThat(invoiceRequest.articlesPrices()).isEqualTo(articlesPrices);
        assertThat(invoiceRequest.totalPrice()).isEqualTo(totalPrice);
    }
}