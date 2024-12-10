package ch.hslu.swda.model.article;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
public record PriceDTO(int francs, int centimes) {
}
