package ch.hslu.swda.persistence;

import ch.hslu.swda.entities.CentralWarehouseOrder;

import java.util.List;
import java.util.UUID;

public interface CentralWarehouseOrderPersistor {

    /**
     * @param order Reference to the centralWarehouseOrder Object. Updates order if it already exists in storage
     */
    void save(CentralWarehouseOrder order);

    /**
     * @param id uuid of the CentralWarehouseOrder to get. returns null if not found.
     */
    CentralWarehouseOrder getById(UUID id);

    /**
     * @param n Get first n orders
     * @return Returns an ordered list with maximum length n of orders which do have articles outstanding.
     */
    List<CentralWarehouseOrder> getFirstOpen(Integer n);

    /**
     * @return Returns a list of orders which do have articles outstanding
     */
    List<CentralWarehouseOrder> getAllOpen();

    /**
     * @return Returns the number of open orders
     */
    int getOpenOrderCount();
}
