package dev.aifudi.backend.repositories;

import dev.aifudi.backend.dtos.db.UserUpdateDataDTO;
import dev.aifudi.backend.dtos.db.UserProfileDTO;
import dev.aifudi.backend.dtos.response.UserResponseDTO;
import dev.aifudi.backend.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findAuthUser(String email);
    Optional<UserProfileDTO> findById(UUID id);
    Optional<UserProfileDTO> findByEmail(String email);
    List<UserResponseDTO> findAllByName(String name, Integer size, Integer offset);
    User save(User user);
    void delete(UUID id);
    void update(UserUpdateDataDTO updateData);
}
