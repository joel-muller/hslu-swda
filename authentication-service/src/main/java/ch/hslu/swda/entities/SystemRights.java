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

    public SystemRights() {
        this.id = UUID.randomUUID();
        this.name = "";
    }

    public SystemRights(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public SystemRights(UUID id, String name) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
