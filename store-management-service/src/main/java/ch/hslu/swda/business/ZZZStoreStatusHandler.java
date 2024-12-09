package ch.hslu.swda.business;

import ch.hslu.swda.entities.Store;
import ch.hslu.swda.messagesIngoing.ZZZStoreStatusRequest;
import ch.hslu.swda.persistence.DatabaseConnector;

import java.util.Map;
import java.util.UUID;

public class ZZZStoreStatusHandler {
    private final DatabaseConnector database;
    public ZZZStoreStatusHandler(DatabaseConnector database){
        this.database = database;
    }
    public void get(ZZZStoreStatusRequest storeStatusRequest) {
        UUID storeId = storeStatusRequest.storeId();
        Store store = database.getStore(storeId);

        Map<String,Integer> params= storeStatusRequest.params();

        if(params.containsKey("orders") && params.get("orders")==1){

        }
        if(params.containsKey("warehouseOrders")&& params.get("warehouseOrders")==1){

        }
        if(params.containsKey("deliveries")&& params.get("deliveries")==1) {

        }
    }




}
