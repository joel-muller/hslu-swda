package ch.hslu.swda.messages;

import java.util.Objects;
import java.util.UUID;

/**
 * Message structure used for either updating or creating a user.
 */
public record UserUpsert(UUID id, String username, String password, String role, UUID employeeId) implements IngoingMessage {


}
