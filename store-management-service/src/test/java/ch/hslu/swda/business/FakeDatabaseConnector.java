package ch.hslu.swda.business;

import ch.hslu.swda.entities.Store;
import ch.hslu.swda.persistence.Data;

import java.util.List;
import java.util.UUID;

public class FakeDatabaseConnector implements Data {
    @Override
    public Store getStore(UUID storeId) {
        return null;
    }

    @Override
    public void storeStore(Store store) {

    }

    @Override
    public List<Store> getAllStores() {
        return List.of();
    }
}
