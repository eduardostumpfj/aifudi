package dev.aifudi.backend.controllers;

import dev.aifudi.backend.dtos.UserRegisterRequestDTO;
import dev.aifudi.backend.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService){
        this.userService = userService;
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

}
