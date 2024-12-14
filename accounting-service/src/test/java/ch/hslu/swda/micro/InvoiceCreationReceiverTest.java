package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.persistence.DatabaseConnector;
import ch.hslu.swda.entities.Invoice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class InvoiceCreationReceiverTest {

    private static final String EXCHANGE_NAME = "exchange";
    private static final String ROUTE = "route";
    private static final String REPLY_TO = "replyTo";
    private static final String CORR_ID = "corrId";
    private static final String MESSAGE = "{\"orderId\":\"4e264294-a921-4fad-8d99-641acfd786ee\",\"customerId\":\"04ff252e-f8ab-49f5-b992-accaf8b04c71\"}";

    private BusConnector bus;
    private DatabaseConnector database;
    private InvoiceCreationReceiver receiver;

    @BeforeEach
    public void setUp() {
        bus = mock(BusConnector.class);
        database = mock(DatabaseConnector.class);
        receiver = new InvoiceCreationReceiver(EXCHANGE_NAME, bus, database);
    }

    @Test
    public void testOnMessageReceived() throws IOException {
        receiver.onMessageReceived(ROUTE, REPLY_TO, CORR_ID, MESSAGE);

        ArgumentCaptor<Invoice> invoiceCaptor = ArgumentCaptor.forClass(Invoice.class);
        verify(database).storeInvoice(invoiceCaptor.capture());
        Invoice storedInvoice = invoiceCaptor.getValue();
        assertThat(storedInvoice.getOrderId().toString()).isEqualTo("4e264294-a921-4fad-8d99-641acfd786ee");
        assertThat(storedInvoice.getCustomerId().toString()).isEqualTo("04ff252e-f8ab-49f5-b992-accaf8b04c71");

        ArgumentCaptor<String> logMessageCaptor = ArgumentCaptor.forClass(String.class);
        verify(bus).talkAsync(eq(EXCHANGE_NAME), eq("logs.new"), logMessageCaptor.capture());
        String logMessage = logMessageCaptor.getValue();
        assertThat(logMessage).contains("Invoice created for orderId 4e264294-a921-4fad-8d99-641acfd786ee and customer 04ff252e-f8ab-49f5-b992-accaf8b04c71");
    }

}