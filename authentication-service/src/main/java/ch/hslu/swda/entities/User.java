package ch.hslu.swda.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.UUID;

/**
 * Represents a user that can be logged into.
 */
@Entity
public class User {
    @Id
    private UUID id;
    private String username;
    private String passwordHash;
    private UserRole role;

    public User() {
        this.id = UUID.randomUUID();
        this.username = "";
        this.passwordHash = "";
        this.role = null;
    }

    public User(String username, String passwordHash, UserRole role) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public User(UUID id, String username, String passwordHash, UserRole role) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }
}
