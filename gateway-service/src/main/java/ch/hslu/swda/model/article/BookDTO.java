package ch.hslu.swda.model.article;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
public record BookDTO(int id, String isbn, String title, String author, String year, String publisher, String imageUrlS, String ImageUrlM, String imageUrlL, PriceDTO price) {
}
