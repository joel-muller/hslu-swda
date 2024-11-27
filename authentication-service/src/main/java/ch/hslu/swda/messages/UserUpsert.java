package ch.hslu.swda.messages;

import java.util.Objects;
import java.util.UUID;

/**
 * Message structure used for either updating or creating a user.
 */
public record UserUpsert(String username, String password, String role, UUID employeeId) implements IngoingMessage {

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
