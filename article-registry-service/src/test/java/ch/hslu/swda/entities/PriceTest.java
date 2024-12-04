package ch.hslu.swda.entities;

import static org.junit.jupiter.api.Assertions.*;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.util.List;

class PriceTest {

    @Test
    void testValidConstructor() {
        Price price = new Price(10, 25);
        assertEquals(10, price.getFrancs());
        assertEquals(25, price.getCentimes());
    }

    @Test
    void testInvalidCentimesConstructor() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Price(10, 23));
        assertEquals("Centimes should be divisible through 5", exception.getMessage());
    }

    @Test
    void testGenerateFromListValid() {
        List<String> priceData = List.of("CHF", "15", "50");
        Price price = Price.generateFromList(priceData);
        assertEquals(15, price.getFrancs());
        assertEquals(50, price.getCentimes());
    }

    @Test
    void testGenerateFromListInvalidCentimes() {
        List<String> priceData = List.of("CHF", "20", "13");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> Price.generateFromList(priceData));
        assertEquals("Centimes should be divisible through 5", exception.getMessage());
    }

    @Test
    void testGenerateFromListInvalidNumberFormat() {
        List<String> priceData = List.of("CHF", "ten", "5");
        assertThrows(NumberFormatException.class, () -> Price.generateFromList(priceData));
    }

    @Test
    void testEqualsAndHashCodeSameValues() {
        Price price1 = new Price(5, 10);
        Price price2 = new Price(5, 10);
        assertEquals(price1, price2);
        assertEquals(price1.hashCode(), price2.hashCode());
    }

    @Test
    void testEqualsAndHashCodeDifferentValues() {
        Price price1 = new Price(5, 10);
        Price price2 = new Price(5, 15);
        assertNotEquals(price1, price2);
        assertNotEquals(price1.hashCode(), price2.hashCode());
    }

    @Test
    void testEqualsWithDifferentObject() {
        Price price = new Price(10, 50);
        assertNotEquals(price, "String");
    }

    @Test
    void testEqualsWithNull() {
        Price price = new Price(10, 50);
        assertNotEquals(price, null);
    }

    @Test
    void testWithEqualsVerifier() {
        EqualsVerifier.forClass(Book.class)
                .withIgnoredFields("id", "title", "author", "year", "publisher", "imageUrlS", "imageUrlM", "imageUrlL", "price") // Test based only on ISBN
                .verify();
    }
}