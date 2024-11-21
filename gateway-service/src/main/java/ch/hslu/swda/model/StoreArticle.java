package ch.hslu.swda.model;
import java.util.UUID;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;


@Introspected @Serdeable
public record StoreArticle(UUID articleId, int quantity, int minimumQuantity, int reservedQuantity) {

}
