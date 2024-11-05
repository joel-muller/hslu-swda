package ch.hslu.swda.model;
import java.util.Map;
import java.util.UUID;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;


@Introspected @Serdeable
public record Order(Map<Integer,Integer> articles, UUID storeId, UUID customerId, UUID employeeId) {

}
