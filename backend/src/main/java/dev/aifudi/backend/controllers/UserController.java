package dev.aifudi.backend.controllers;

import dev.aifudi.backend.dtos.request.UserRegisterRequestDTO;
import dev.aifudi.backend.dtos.request.UserUpdateRequestDTO;
import dev.aifudi.backend.entities.User;
import dev.aifudi.backend.services.AuthService;
import dev.aifudi.backend.services.PermissionService;
import dev.aifudi.backend.services.UserService;
import dev.aifudi.backend.services.exceptions.AccessDeniedException;
import dev.aifudi.backend.services.exceptions.InvalidRegisterException;
import dev.aifudi.backend.services.exceptions.NotFoundException;
import dev.aifudi.backend.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final PermissionService permissionService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    public UserController(UserService userService, AuthService authService, PermissionService permissionService){
        this.userService = userService;
        this.authService = authService;
        this.permissionService = permissionService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(
            @Valid
            @RequestBody UserRegisterRequestDTO user
            ){
        logger.info("POST -> /register");

        this.userService.registerUser(user);
        return ResponseEntity.status(200).build();
    }

    @PutMapping("/{email}")
    public ResponseEntity<Void> updateUserRegister(
            @PathVariable String email,

            @RequestHeader(value = "Authorization")
            String authHeader,

            @Valid
            @RequestBody UserUpdateRequestDTO updateUser
    ){

        logger.info("PUT -> /{email}");
        // check authentication
        User authUser = this.authService.authenticateUser(authHeader);

        logger.info("Usuário autenticado");
        // Check permission
        this.permissionService.checkRegisterUpdatePermission(authUser, email);
        logger.info("Usuário autorizado");

        // Update User
        this.userService.updateUserRegister(updateUser, email);
        logger.info("Usuário Atualizado");

        return ResponseEntity.status(204).build();
    }

}
