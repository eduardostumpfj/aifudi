package dev.aifudi.backend.services;

import dev.aifudi.backend.dtos.*;
import dev.aifudi.backend.dtos.AddressUpdateDataDTO;
import dev.aifudi.backend.dtos.UserUpdateDataDTO;
import dev.aifudi.backend.dtos.UserUpdateRequestDTO;
import dev.aifudi.backend.entities.Address;
import dev.aifudi.backend.entities.Role;
import dev.aifudi.backend.entities.User;
import dev.aifudi.backend.repositories.AddressRepositoryImp;
import dev.aifudi.backend.repositories.RoleRepositoryImp;
import dev.aifudi.backend.repositories.UserRepositoryImp;
import dev.aifudi.backend.services.exceptions.InvalidRegisterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final RoleRepositoryImp roleRepositoryImp;
    private final UserRepositoryImp userRepositoryImp;
    private final AddressRepositoryImp addressRepositoryImp;
    private final PasswordEncoder passwordEncoder;

    public UserService(RoleRepositoryImp roleRepositoryImp, PasswordEncoder passwordEncoder, UserRepositoryImp userRepositoryImp, AddressRepositoryImp addressRepositoryImp){
        this.roleRepositoryImp = roleRepositoryImp;
        this.passwordEncoder = passwordEncoder;
        this.userRepositoryImp = userRepositoryImp;
        this.addressRepositoryImp = addressRepositoryImp;
    }

    public void registerUser(UserRegisterRequestDTO user){
        // Find Role
        Role role = findRole(user.roleName());
        logger.info("Role_Id Encontrado");

        // HashPassword
        String hashedPassword = this.passwordEncoder.encode(user.password());
        logger.info("Password criptografado");

        // Save User
        var savedUser = this.userRepositoryImp.save(new User(
                null,
                user.name(),
                user.email(),
                hashedPassword,
                role.getId()
        ));
        logger.info("Usuário Salvo: " + savedUser.getName());

        // Save Address
        this.addressRepositoryImp.save(new Address(
                null,
                savedUser.getId(),
                user.cep(),
                user.state(),
                user.city(),
                user.address(),
                user.number(),
                user.complement()
        ));

    }

    @Transactional
    public void updateUserRegister(UserUpdateRequestDTO updateUser, UUID userId){
        // Update User
        UUID roleId = null;

        if (updateUser.roleName() != null) {
            roleId = findRole(updateUser.roleName()).getId();
        }
        UserUpdateDataDTO userData = new UserUpdateDataDTO(
                userId,
                updateUser.name(),
                updateUser.email(),
                roleId
        );
        this.userRepositoryImp.update(userData);

        // Update Address
        AddressUpdateDataDTO addressData = new AddressUpdateDataDTO(
                userId,
                updateUser.cep(),
                updateUser.state(),
                updateUser.city(),
                updateUser.address(),
                updateUser.number(),
                updateUser.complement()
        );
        this.addressRepositoryImp.update(addressData);

    }

    public Role findRole(String name){
        var role = this.roleRepositoryImp.findRoleByName(name);
        if(role.isEmpty()){
            throw new InvalidRegisterException("Role", "Invalid Role");
        }
        return role.get();
    }

}
