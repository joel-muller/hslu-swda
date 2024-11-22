package ch.hslu.swda.business;

import ch.hslu.swda.entities.User;
import ch.hslu.swda.entities.UserRole;

import java.util.List;
import java.util.UUID;

/**
 * Interface for storing/interacting with Users
 */
public interface Users {

    /**
     * Stores a user.
     * @param user
     * @return true on success, false on failure
     */
    boolean addUser(User user);

    /**
     * Returns a user based on their id.
     * @param id
     * @return User
     */
    User getById(UUID id);

    /**
     * Returns a user based on their username.
     * @param username
     * @return User
     */
    User getByUsername(String username);

    /**
     * Returns a list of all users.
     * @return List of users.
     */
    List<User> getAll();

    /**
     * Deletes a user from the storage.
     * @param id
     * @return true on success, false on failure
     */
    boolean deleteUser(UUID id);

    /**
     * Updates a user's username.
     * @param id
     * @param username
     * @return true on success, false on failure
     */
    boolean updateUsername(UUID id, String username);

    /**
     * Updates a user's password hash.
     * @param passwordHash
     * @return true on success, false on failure
     */
    boolean updatePasswordHash(UUID id, String passwordHash);

    /**
     * Updates a user's role.
     * @param id
     * @param role
     * @return true on success, false on failure
     */
    boolean updateUserRole(UUID id, UserRole role);

    /**
     * Updates multiple properties of a user.
     * @param id
     * @param username
     * @param passwordHash
     * @param role
     * @return
     */
    boolean updateUser(UUID id, String username, String passwordHash, UserRole role);
}
