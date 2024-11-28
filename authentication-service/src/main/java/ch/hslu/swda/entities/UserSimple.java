package ch.hslu.swda.entities;

import java.util.UUID;

public record UserSimple(UUID id, String username, String roleName) {
}
