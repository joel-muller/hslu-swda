package ch.hslu.swda.messages;

import java.util.Objects;
import java.util.UUID;

/**
 * Message structure used for either updating or creating a user.
 */
public class UserUpsert implements IngoingMessage {
    private final String username;
    private final String password;
    private final String role;
    private final UUID employeeId;

    public UserUpsert(String username, String password, String role, UUID employeeId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.employeeId = employeeId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserUpsert that = (UserUpsert) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, role);
    }

    @Override
    public String toString() {
        return "UserCreate{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
