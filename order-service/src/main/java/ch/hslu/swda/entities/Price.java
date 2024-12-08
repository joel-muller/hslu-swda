package ch.hslu.swda.entities;


import java.util.List;
import java.util.Objects;

public final class Price {
    private final int francs;
    private final int centimes;

    public Price(int francs, int centimes) {
        this.francs = francs + (centimes / 100);
        if (centimes % 5 != 0 || centimes < 0 || francs < 0) {
            throw new IllegalArgumentException("Francs and Centimes should be over 0 and Centimes should be divisible through 5");
        }
        this.centimes = centimes % 100;
    }

    public static Price generateFromList(List<String> list) throws NumberFormatException {
        int francs = Integer.parseInt(list.get(1));
        int centimes = Integer.parseInt(list.get(2));
        return new Price(francs, centimes);
    }

    public int getFrancs() {
        return francs;
    }

    public int getCentimes() {
        return centimes;
    }

    public Price addAnother(Price price) {
        int francs = this.francs + price.getFrancs() + ((this.centimes + price.getCentimes()) / 100);
        int centimes = (this.centimes + price.getCentimes()) % 100 ;
        return new Price(francs, centimes);
    }

    public static Price multiplyPrice(Price price, int multiplier) {
        int francs = price.getFrancs() * multiplier;
        int centimes = price.getCentimes() * multiplier;
        return new Price(francs, centimes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Price price)) return false;
        return francs == price.francs && centimes == price.centimes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(francs, centimes);
    }

    @Override
    public String toString() {
        return "Price{" + getInvoiceString() + " Francs}";
    }

    public String getInvoiceString() {
        if (centimes < 10) {
            return this.francs + ".0" + this.centimes;
        }
        return this.francs + "." + this.centimes;
    }
}
