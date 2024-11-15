package ch.hslu.swda.entities;

public interface CentralWarehouseOrderMapper<T> {
    T fromCentralWarehouseOrder(CentralWarehouseOrder order);

    CentralWarehouseOrder toCentralWarehouseOrder(T other);
}
