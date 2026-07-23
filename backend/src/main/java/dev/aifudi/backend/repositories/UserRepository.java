package dev.aifudi.backend.repositories;

import dev.aifudi.backend.dtos.UserResponseDTO;
import dev.aifudi.backend.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    List<UserResponseDTO> findAllByName(String name, int size, int offset);
    User save(User user);
    Integer delete(UUID id);
    Integer upDate(User user);
}
