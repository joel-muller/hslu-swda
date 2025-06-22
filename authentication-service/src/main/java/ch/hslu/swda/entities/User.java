package ch.hslu.swda.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.Objects;
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

    /**
     * Default constructor
     */
    public User() {
        this.id = UUID.randomUUID();
        this.username = "";
        this.passwordHash = "";
        this.role = null;
    }

    /**
     * Constructor to be used to add new users
     * @param username
     * @param passwordHash
     * @param role
     */
    public User(String username, String passwordHash, UserRole role) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    /**
     * General constructor
     * @param id
     * @param username
     * @param passwordHash
     * @param role
     */
    public User(UUID id, String username, String passwordHash, UserRole role) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    /**
     * Method to remove hash before returning user to API.
     * @param user
     * @return User without passwordHash
     */
    public static User removeHashForReturn(User user) {
        return new User(user.getId(), user.getUsername(), null, user.getRole());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", role=" + role +
                '}';
    }
}
