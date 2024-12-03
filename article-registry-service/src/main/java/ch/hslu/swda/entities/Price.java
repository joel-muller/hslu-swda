package ch.hslu.swda.entities;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.List;
import java.util.Objects;

public class Price {
    private final int franken;
    private final int rappen;

    public Price(int franken, int rappen) {
        this.franken = franken;
        if (rappen % 5 != 0) {
            throw new IllegalArgumentException("Rappen ist nicht auch 5 genau");
        }
        this.rappen = rappen;
    }

    public static Price generateFromList(List<String> list) throws NumberFormatException {
        int franken = Integer.parseInt(list.get(1));
        int rappen = Integer.parseInt(list.get(2));
        return new Price(franken, rappen);
    }

    public int getFranken() {
        return franken;
    }

    public int getRappen() {
        return rappen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Price price)) return false;
        return franken == price.franken && rappen == price.rappen;
    }

    @Override
    public int hashCode() {
        return Objects.hash(franken, rappen);
    }
}
