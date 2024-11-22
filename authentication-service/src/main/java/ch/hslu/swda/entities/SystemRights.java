package ch.hslu.swda.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.UUID;

/**
 * Represents a set of permissions for a service.
 */
@Entity
public class SystemRights {
    @Id
    private UUID id;
    private String name;
    private int bitmask;

    public SystemRights() {
        this.id = UUID.randomUUID();
        this.name = "";
        this.bitmask = 0b0000;
    }

    public SystemRights(String name, int bitmask) {
        if (bitmask > 0b1111 || bitmask < 0) {
            throw new IllegalArgumentException("Bitmask is invalid");
        }
        this.id = UUID.randomUUID();
        this.name = name;
        this.bitmask = bitmask;
    }

    public SystemRights(UUID id, String name, int bitmask) {
        if (bitmask > 0b1111 || bitmask < 0) {
            throw new IllegalArgumentException("Bitmask is invalid");
        }
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        this.id = id;
        this.name = name;
        this.bitmask = bitmask;
    }
}
