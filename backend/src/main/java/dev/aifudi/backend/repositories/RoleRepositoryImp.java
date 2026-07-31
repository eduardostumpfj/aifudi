package dev.aifudi.backend.repositories;

import dev.aifudi.backend.entities.Role;
import dev.aifudi.backend.enums.RoleName;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class RoleRepositoryImp implements RoleRepository {
    private  final JdbcClient jdbcClient;

    public RoleRepositoryImp(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<Role> findRoleByName(RoleName name) {
        return this.jdbcClient
                .sql("SELECT * FROM roles WHERE name = :name")
                .param("name", name)
                .query(Role.class)
                .optional();
    }

    @Override
    public Optional<Role> findRoleById(UUID roleId) {
        return this.jdbcClient
                .sql("SELECT * FROM roles WHERE id = :roleId")
                .param("roleId", roleId)
                .query(Role.class)
                .optional();
    }
}
