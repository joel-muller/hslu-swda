package ch.hslu.swda.messages;

import java.util.Objects;
import java.util.UUID;

public record UserDelete(UUID userId, UUID employeeId) implements IngoingMessage {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDelete that = (UserDelete) o;
        return Objects.equals(userId, that.userId) && Objects.equals(employeeId, that.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, employeeId);
    }

    @Override
    public String toString() {
        return "UserDelete{" +
                "userId=" + userId +
                ", employeeId=" + employeeId +
                '}';
    }
}
