package ch.hslu.swda.persistence;

import ch.hslu.swda.entities.CentralWarehouseOrder;

import java.util.List;
import java.util.UUID;

public class DatabasePersistor implements CentralWarehouseOrderPersistor{
    @Override
    public void save(CentralWarehouseOrder order) {

    }

    @Override
    public CentralWarehouseOrder getById(UUID id) {
        return null;
    }

    @Override
    public List<CentralWarehouseOrder> getFirstOpen(Integer n) {
        return null;
    }

    @Override
    public List<CentralWarehouseOrder> getAllOpen() {
        return null;
    }

    @Override
    public int getOpenOrderCount() {
        return 0;
    }
}
