package ch.hslu.swda.micro;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.business.StoreStatusHandler;
import ch.hslu.swda.messagesIngoing.StoreStatusRequest;
import ch.hslu.swda.persistence.DatabaseConnector;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class StoreStatusReceiver implements MessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(StoreStatusRequest.class);

    private final DatabaseConnector database;
    private final StoreManagementService service;
    private final String exchangeName;
    private final BusConnector bus;

    private final StoreStatusHandler storeStatusHandler;

    public StoreStatusReceiver(final DatabaseConnector database, final String exchangeName, final BusConnector bus, final StoreManagementService service) {
        this.exchangeName = exchangeName;
        this.bus = bus;
        this.database = database;
        this.service = service;
        this.storeStatusHandler = new StoreStatusHandler(database);
    }
    @Override
    public void onMessageReceived(String route, String replyTo, String corrId, String message) {
        LOG.info("Received Message on route: {}, replyTo: {}, corrId: {}, message:{}",route,replyTo,corrId,message);
        ObjectMapper mapper  = new ObjectMapper();
        StoreStatusRequest storeStatusRequest = null;
        try{
            storeStatusRequest = mapper.readValue(message,StoreStatusRequest.class);
        }catch (IOException e){
            LOG.error("Error occurred while reading message: {}", e.getMessage());
        }

        if (storeStatusRequest==null) return;

        storeStatusHandler.get(storeStatusRequest);


    }
}
