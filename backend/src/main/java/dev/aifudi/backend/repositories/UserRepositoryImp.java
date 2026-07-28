package dev.aifudi.backend.repositories;

import dev.aifudi.backend.dtos.db.UserProfileDTO;
import dev.aifudi.backend.dtos.db.UserUpdateDataDTO;
import dev.aifudi.backend.entities.User;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepositoryImp implements UserRepository{
    private final JdbcClient jdbcClient;

    public UserRepositoryImp(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<UserProfileDTO> findById(UUID id) {
        return this.jdbcClient
                .sql("SELECT" +
                        "    u.id, " +
                        "    u.name," +
                        "    u.email," +
                        "    r.name AS role_name," +
                        "    a.id AS addressId, " +
                        "    a.cep," +
                        "    a.state," +
                        "    a.city," +
                        "    a.address," +
                        "    a.address_number AS number," +
                        "    a.complement " +
                        "FROM users u " +
                        "INNER JOIN address a " +
                        "    ON u.id = a.user_id " +
                        "INNER JOIN roles r " +
                        "    ON u.role_id = r.id " +
                        "WHERE u.id = :id;")
                .param("id", id)
                .query(UserProfileDTO.class)
                .optional();
    }

    @Override
    public Optional<UserProfileDTO> findByEmail(String email) {
        System.out.println("Entrei no findByEmail");
        return this.jdbcClient
                .sql("SELECT" +
                        "    u.id, " +
                        "    u.name," +
                        "    u.email," +
                        "    r.name AS role_name," +
                        "    a.cep," +
                        "    a.state," +
                        "    a.city," +
                        "    a.address," +
                        "    a.address_number AS number," +
                        "    a.complement " +
                        "FROM users u " +
                        "INNER JOIN address a " +
                        "    ON u.id = a.user_id " +
                        "INNER JOIN roles r " +
                        "    ON u.role_id = r.id " +
                        "WHERE u.email = :email;")
                .param("email", email)
                .query(UserProfileDTO.class)
                .optional();
    }

    @Override
    public Optional<User> findAuthUser(String email) {
        return this.jdbcClient
                .sql("SELECT * FROM users WHERE email = :email;")
                .param("email", email)
                .query(User.class)
                .optional();
    }

    @Override
    public List<UserProfileDTO> findAllByName(String name, int size, int offset) {
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
    public void update(UserUpdateDataDTO updateData) {
        StringBuilder sql = new StringBuilder("UPDATE users SET ");
        Map<String, Object> params = new HashMap<>();

        if (updateData.name() != null) {
            sql.append("name = :name, ");
            params.put("name",updateData.name());
        }

        if (updateData.email() != null) {
            sql.append("email = :email, ");
            params.put("email", updateData.email());
        }

        if (params.isEmpty()) {
            return;
        }

        sql.setLength(sql.length() - 2);

        sql.append(" WHERE id = :id");
        params.put("id", updateData.id());

        JdbcClient.StatementSpec stmt = jdbcClient.sql(sql.toString());
        params.forEach(stmt::param);
        stmt.update();
    }
}
