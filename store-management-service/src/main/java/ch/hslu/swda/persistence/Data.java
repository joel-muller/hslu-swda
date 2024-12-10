package ch.hslu.swda.persistence;

import ch.hslu.swda.entities.Store;

import java.util.List;
import java.util.UUID;

public interface Data {
    public Store getStore(UUID storeId);
    public void storeStore(Store store);
    public List<Store> getAllStores();
}
