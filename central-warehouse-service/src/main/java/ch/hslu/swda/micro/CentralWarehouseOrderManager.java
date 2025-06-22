package ch.hslu.swda.micro;

import ch.hslu.swda.entities.CentralWarehouseOrder;

import java.util.UUID;

public interface CentralWarehouseOrderManager {
    /**
     *
     * @param order CentralWarehouseOrder that needs to be processed
     */
    void process(CentralWarehouseOrder order);

}
