package dev.aifudi.backend.services;

import dev.aifudi.backend.dtos.db.AddressUpdateDataDTO;
import dev.aifudi.backend.dtos.db.UserProfileDTO;
import dev.aifudi.backend.dtos.db.UserUpdateDataDTO;
import dev.aifudi.backend.dtos.request.UserRegisterRequestDTO;
import dev.aifudi.backend.dtos.request.UserUpdateRequestDTO;
import dev.aifudi.backend.entities.Address;
import dev.aifudi.backend.entities.Role;
import dev.aifudi.backend.entities.User;
import dev.aifudi.backend.repositories.AddressRepositoryImp;
import dev.aifudi.backend.repositories.RoleRepositoryImp;
import dev.aifudi.backend.repositories.UserRepositoryImp;
import dev.aifudi.backend.services.exceptions.AccessDeniedException;
import dev.aifudi.backend.services.exceptions.InvalidRegisterException;
import dev.aifudi.backend.services.exceptions.NotFoundException;
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

        // HashPassword
        String hashedPassword = this.passwordEncoder.encode(user.password());

        // Save User
        var savedUser = this.userRepositoryImp.save(new User(
                null,
                user.name(),
                user.email(),
                hashedPassword,
                role.getId()
        ));

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
    public void updateUserRegister(UserUpdateRequestDTO updateUser, String requestEmail){
        // Update User
        // Get URL User
        UserProfileDTO foundUser = this.userRepositoryImp.findByEmail(requestEmail).orElseThrow(() -> new NotFoundException("User not Found"));

        // In this route app users cannot change their roles.
        UserUpdateDataDTO userData = new UserUpdateDataDTO(
                foundUser.id(),
                updateUser.name(),
                updateUser.email(),
                null
        );
        this.userRepositoryImp.update(userData);

        // Update Address
        AddressUpdateDataDTO addressData = new AddressUpdateDataDTO(
                foundUser.id(),
                updateUser.cep(),
                updateUser.state(),
                updateUser.city(),
                updateUser.address(),
                updateUser.number(),
                updateUser.complement()
        );
        this.addressRepositoryImp.update(addressData);
    }

    public void deleteUserRegister(String requestEmail){
        UserProfileDTO foundUser = this.userRepositoryImp.findByEmail(requestEmail).orElseThrow(() -> new NotFoundException("User not Found"));
        this.userRepositoryImp.delete(foundUser.id());
    }

    public void resetUserPassword(User authUser, String password){
        // Check same password
        boolean matchedPassword = this.passwordEncoder.matches(password ,authUser.getHashedPassword());
        if(matchedPassword){
            throw new InvalidRegisterException("password", "The passwords cannot be the same");
        }

        // HashPassword
        String hashedPassword = this.passwordEncoder.encode(password);

        UserUpdateDataDTO userData = new UserUpdateDataDTO(
                authUser.getId(),
                null,
                null,
                hashedPassword
        );

        this.userRepositoryImp.update(userData);

    }

    public Role findRole(String name){
        var role = this.roleRepositoryImp.findRoleByName(name);
        if(role.isEmpty()){
            throw new InvalidRegisterException("Role", "Invalid Role");
        }
        return role.get();
    }

}
