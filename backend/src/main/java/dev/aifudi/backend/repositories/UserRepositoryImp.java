package dev.aifudi.backend.repositories;

import dev.aifudi.backend.dtos.UserResponseDTO;
import dev.aifudi.backend.entities.User;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImp implements UserRepository{
    private final JdbcClient jdbcClient;

    public UserRepositoryImp(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public List<UserResponseDTO> findAllByName(String name, int size, int offset) {
        return List.of();
    }

    @Override
    public User save(User user) {
        return this.jdbcClient.
                sql("INSERT INTO users (name, role_id, email, hashed_password ) VALUES (:name, :role_id, :email, :hashed_password) RETURNING id, name, role_id, email, hashed_password")
                .param("name", user.getName())
                .param("role_id", user.getRoleId())
                .param("email", user.getEmail())
                .param("hashed_password", user.getHashedPassword())
                .query((rs, rowNum) -> {
                    User savedUser = new User();
                    savedUser.setId(rs.getObject("id", java.util.UUID.class));
                    savedUser.setName(rs.getString("name"));
                    savedUser.setRoleId(rs.getObject("role_id", java.util.UUID.class));
                    savedUser.setEmail(rs.getString("email"));
                    savedUser.setHashedPassword(rs.getString("hashed_password"));
                    return savedUser;
                })
                .single();
    }

    @Override
    public Integer delete(UUID id) {
        return 0;
    }

    @Override
    public Integer upDate(User user) {
        return 0;
    }
}
