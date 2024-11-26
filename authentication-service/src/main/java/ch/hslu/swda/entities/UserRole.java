package ch.hslu.swda.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Represents a role that can be assigned to an existing user.
 */
@Entity
public class UserRole {
    @Id
    private UUID id;
    private String name;
    private List<SystemRights> rights;

    public UserRole() {
        this.id = UUID.randomUUID();
        this.name = "";
        this.rights = Collections.emptyList();
    }

    public UserRole(String name, List<SystemRights> rights) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.rights = rights;
    }

    public UserRole(UUID id, String name, List<SystemRights> rights) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        this.id = id;
        this.name = name;
        this.rights = rights;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<SystemRights> getRights() {
        return rights;
    }
}
