package ch.hslu.swda.micro;

import ch.hslu.swda.business.DatabaseConnector;
import ch.hslu.swda.entities.Store;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;




public class StoreCreationRecieverTest {

    private DatabaseConnector database;
    private StoreManagementService service;
    private StoreCreationReciever receiver;
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        database = mock(DatabaseConnector.class);
        service = mock(StoreManagementService.class);
        receiver = new StoreCreationReciever(database, service);
        mapper = new ObjectMapper();
        }

        @Test
        public void testOnMessageReceived_success() throws IOException {
        String message = "{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"articleList\":[]}";
        Store store = mapper.readValue(message, Store.class);

        receiver.onMessageReceived("route", "replyTo", "corrId", message);

        ArgumentCaptor<Store> storeCaptor = ArgumentCaptor.forClass(Store.class);
        verify(database, times(1)).saveStoreObject(storeCaptor.capture());
        assertThat(storeCaptor.getValue().getId()).isEqualTo(store.getId());
        assertThat(storeCaptor.getValue().getArticleList()).isEqualTo(store.getArticleList());
        }

        @Test
        public void testOnMessageReceived_failure() {
        String invalidMessage = "invalid json";

        receiver.onMessageReceived("route", "replyTo", "corrId", invalidMessage);

        verify(database, never()).saveStoreObject(any(Store.class));
    }
}