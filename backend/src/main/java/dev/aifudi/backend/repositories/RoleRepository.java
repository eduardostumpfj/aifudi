package dev.aifudi.backend.repositories;

import dev.aifudi.backend.entities.Role;
import dev.aifudi.backend.enums.RoleName;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository {
    Optional<Role> findRoleByName(RoleName name);
    Optional<Role> findRoleById(UUID roleId);
}
