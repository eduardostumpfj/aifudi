package dev.aifudi.backend.repositories;

import dev.aifudi.backend.entities.Role;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository {
    Optional<Role> findRoleByName(String name);
    Optional<Role> findRoleById(UUID roleId);
}
