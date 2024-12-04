package ch.hslu.swda.entities;

import java.util.List;
import java.util.Objects;

public final class Price {
    private final int francs;
    private final int centimes;

    public Price(int francs, int centimes) {
        this.francs = francs;
        if (centimes % 5 != 0) {
            throw new IllegalArgumentException("Centimes should be divisible through 5");
        }
        this.centimes = centimes;
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
}
