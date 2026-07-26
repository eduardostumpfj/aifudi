package dev.aifudi.backend.services;

import dev.aifudi.backend.dtos.UserAuthDTO;
import dev.aifudi.backend.repositories.UserRepositoryImp;
import dev.aifudi.backend.services.exceptions.FailedAuthException;
import dev.aifudi.backend.services.exceptions.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {
    private final UserRepositoryImp userRepositoryImp;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepositoryImp userRepositoryImp, PasswordEncoder passwordEncoder){
        this.userRepositoryImp = userRepositoryImp;
        this.passwordEncoder = passwordEncoder;
    }

    public UUID authenticateUser(UserAuthDTO user){
        // Find user
        var foundUser = this.userRepositoryImp.findAuthUser(user.email());
        if(foundUser.isEmpty()){
            throw new ResourceNotFoundException("User not found");
        }

        // Check Password
        boolean matchedPassword = this.passwordEncoder.matches(user.password(), foundUser.get().getHashedPassword());
        if(!matchedPassword){
            throw new FailedAuthException("Invalid Credentials");
        }

        return foundUser.get().getId();
    }
}
