package dev.aifudi.backend.repositories;

import dev.aifudi.backend.dtos.db.UserUpdateDataDTO;
import dev.aifudi.backend.dtos.db.UserProfileDTO;
import dev.aifudi.backend.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findAuthUser(String email);
    Optional<UserProfileDTO> findById(UUID id);
    Optional<UserProfileDTO> findByEmail(String email);
    List<UserProfileDTO> findAllByName(String name, int size, int offset);
    User save(User user);
    Integer delete(UUID id);
    void update(UserUpdateDataDTO updateData);
}
