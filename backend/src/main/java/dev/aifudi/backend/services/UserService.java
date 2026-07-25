package dev.aifudi.backend.services;

import dev.aifudi.backend.dtos.UserRegisterRequestDTO;
import dev.aifudi.backend.entities.Address;
import dev.aifudi.backend.entities.User;
import dev.aifudi.backend.repositories.AddressRepositoryImp;
import dev.aifudi.backend.repositories.RoleRepositoryImp;
import dev.aifudi.backend.repositories.UserRepositoryImp;
import dev.aifudi.backend.services.exceptions.InvalidRegisterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        var role = this.roleRepositoryImp.findRoleByName(user.roleName());
        if(role.isEmpty()){
            throw new InvalidRegisterException("Role", "Invalid Role");
        }
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
                role.get().getId()
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



}
