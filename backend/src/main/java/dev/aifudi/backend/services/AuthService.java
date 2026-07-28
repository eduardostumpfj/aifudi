package dev.aifudi.backend.services;

import dev.aifudi.backend.entities.User;
import dev.aifudi.backend.repositories.UserRepositoryImp;
import dev.aifudi.backend.services.exceptions.FailedAuthException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepositoryImp userRepositoryImp;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepositoryImp userRepositoryImp, PasswordEncoder passwordEncoder){
        this.userRepositoryImp = userRepositoryImp;
        this.passwordEncoder = passwordEncoder;
    }

    public User authenticateUser(String authHeader){

        // Format Credentials
        String base64Credentials = authHeader.substring(6).trim();
        if(!authHeader.startsWith("Basic ")){
            throw new FailedAuthException("Invalid Credentials");
        }

        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        String[] values = credentials.split(":", 2);
        String userEmail = values[0];
        String userPassword = values[1];

        // Find user
        Optional<User> foundUser = this.userRepositoryImp.findAuthUser(userEmail);
        if(foundUser.isEmpty()){
            throw new FailedAuthException("Invalid Credentials");
        }

        // Check Password
        boolean matchedPassword = this.passwordEncoder.matches(userPassword, foundUser.get().getHashedPassword());
        if(!matchedPassword){
            throw new FailedAuthException("Invalid Credentials");
        }

        return foundUser.get();
    }
}
