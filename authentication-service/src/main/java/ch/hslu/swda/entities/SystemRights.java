package ch.hslu.swda.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SystemRights that = (SystemRights) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SystemRights{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
