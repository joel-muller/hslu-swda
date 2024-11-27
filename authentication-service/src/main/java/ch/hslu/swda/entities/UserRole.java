package ch.hslu.swda.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

    /**
     * Default constructor
     */
    public UserRole() {
        this.id = UUID.randomUUID();
        this.name = "";
        this.rights = Collections.emptyList();
    }

    /**
     * Constructor to be used to add new user roles
     * @param name
     * @param rights
     */
    public UserRole(String name, List<SystemRights> rights) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.rights = rights;
    }

    /**
     * General constructor
     * @param id
     * @param name
     * @param rights
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        return Objects.equals(id, userRole.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rights=" + rights +
                '}';
    }
}
