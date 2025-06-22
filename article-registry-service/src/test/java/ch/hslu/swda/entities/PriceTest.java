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
    void testValidConstructorToMuchCentimes1() {
        Price price = new Price(10, 3010);
        assertEquals(40, price.getFrancs());
        assertEquals(10, price.getCentimes());
    }

    @Test
    void testValidConstructorToMuchCentimes2() {
        Price price = new Price(10, 105);
        assertEquals(11, price.getFrancs());
        assertEquals(5, price.getCentimes());
    }

    @Test
    void testInvalidCentimesConstructor() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Price(10, 23));
        assertEquals("Francs and Centimes should be over 0 and Centimes should be divisible through 5", exception.getMessage());
    }

    @Test
    void testInvalidCentimesConstructorNegativeFrancs() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Price(-10, 0));
        assertEquals("Francs and Centimes should be over 0 and Centimes should be divisible through 5", exception.getMessage());
    }

    @Test
    void testInvalidCentimesConstructorNegativeCentimes() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Price(10, -5));
        assertEquals("Francs and Centimes should be over 0 and Centimes should be divisible through 5", exception.getMessage());
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
        assertEquals("Francs and Centimes should be over 0 and Centimes should be divisible through 5", exception.getMessage());
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
        EqualsVerifier.forClass(Price.class)
                .verify();
    }

    @Test
    void testAddAnotherWithSimpleAddition() {
        Price price1 = new Price(5, 50);
        Price price2 = new Price(3, 20);
        Price result = price1.addAnother(price2);

        assertEquals(new Price(8, 70), result, "Addition of 5.50 and 3.20 should result in 8.70");
    }

    @Test
    void testAddAnotherWithCentimesOverflow() {
        Price price1 = new Price(4, 80);
        Price price2 = new Price(2, 50);
        Price result = price1.addAnother(price2);

        assertEquals(new Price(7, 30), result, "Addition of 4.80 and 2.50 should result in 7.30");
    }

    @Test
    void testAddAnotherWithZeroValues() {
        Price price1 = new Price(0, 0);
        Price price2 = new Price(0, 0);
        Price result = price1.addAnother(price2);

        assertEquals(new Price(0, 0), result, "Addition of 0.00 and 0.00 should result in 0.00");
    }

    @Test
    void testAddAnotherWithOneZeroValue() {
        Price price1 = new Price(5, 25);
        Price price2 = new Price(0, 0);
        Price result = price1.addAnother(price2);

        assertEquals(new Price(5, 25), result, "Addition of 5.25 and 0.00 should result in 5.25");
    }

    @Test
    void testAddAnotherWithMaximumCentimes() {
        Price price1 = new Price(3, 95);
        Price price2 = new Price(0, 10);
        Price result = price1.addAnother(price2);

        assertEquals(new Price(4, 5), result, "Addition of 3.95 and 0.10 should result in 4.05" + result.toString());
    }

    @Test
    void testAddAnotherWithNegativeThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Price(-1, 50);
        }, "Creating a Price with negative francs should throw IllegalArgumentException");

        assertThrows(IllegalArgumentException.class, () -> {
            new Price(3, -5);
        }, "Creating a Price with negative centimes should throw IllegalArgumentException");
    }

    @Test
    void testToString() {
        assertEquals("Price{5.05 Francs}", new Price(5, 5).toString());
        assertEquals("Price{8.15 Francs}", new Price(8, 15).toString());
    }
}