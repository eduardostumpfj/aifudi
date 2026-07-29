package dev.aifudi.backend.controllers;

import dev.aifudi.backend.dtos.request.UserResetPasswordRequestDTO;
import dev.aifudi.backend.dtos.request.UserRegisterRequestDTO;
import dev.aifudi.backend.dtos.request.UserUpdateRequestDTO;
import dev.aifudi.backend.entities.User;
import dev.aifudi.backend.services.AuthService;
import dev.aifudi.backend.services.PermissionService;
import dev.aifudi.backend.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
        // Register doesn't need auth
        // Check Permissions
        this.permissionService.checkRegisterUserPermission(user);

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

        // Check permission
        this.permissionService.checkRegisterUpdatePermission(authUser, email);

        // Update User
        this.userService.updateUserRegister(updateUser, email);

        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteRegister(
            @PathVariable String email,

            @RequestHeader(value = "Authorization")
            String authHeader
    ){
        logger.info("DELETE -> /{email}");
        // check authentication
        User authUser = this.authService.authenticateUser(authHeader);

        // Check permission
        this.permissionService.checkDeleteUserPermission(authUser, email);

        // Delete User Data (register and address)
        this.userService.deleteUserRegister(email);

        return ResponseEntity.status(204).build();
    }


    @PostMapping("/{email}/password")
    public ResponseEntity<Void> updatePassword(
            @Valid
            @RequestBody UserResetPasswordRequestDTO password,

            @PathVariable String email,

            @RequestHeader(value = "Authorization") String authHeader
            ){
        logger.info("UPDATE PASSWORD -> /{email}/password");

        // check authentication
        User authUser = this.authService.authenticateUser(authHeader);

        // check permissions
        this.permissionService.checkResetPasswordPermission(authUser, email);

        //Reset Password
        this.userService.resetUserPassword(authUser, password.password());

        return ResponseEntity.status(204).build();
    }
}
