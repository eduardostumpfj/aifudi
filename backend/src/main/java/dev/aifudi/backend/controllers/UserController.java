package dev.aifudi.backend.controllers;

import dev.aifudi.backend.dtos.request.UserResetPasswordRequestDTO;
import dev.aifudi.backend.dtos.request.UserRegisterRequestDTO;
import dev.aifudi.backend.dtos.request.UserUpdateRequestDTO;
import dev.aifudi.backend.dtos.response.UserResponseDTO;
import dev.aifudi.backend.entities.User;
import dev.aifudi.backend.services.AuthService;
import dev.aifudi.backend.services.PermissionService;
import dev.aifudi.backend.services.UserService;
import dev.aifudi.backend.services.exceptions.InvalidParamException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    @Operation(
            tags = "Register User",
            description = "This operation provides support for creating a new user profile in our system." +
                    "The caller's email and login must be unique and cannot be used for another account." +
                    "<br>" +
                    "Note: This endpoint does not allow the creation of `ADMIN` users.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden. This endpoint does not allow the creation of ADMIN users.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict. This email is already in use by another account.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "The server encountered an unexpected error.",
                            content = @Content
                    ),

            }

    )
    public ResponseEntity<Void> registerUser(
            @Valid
            @RequestBody UserRegisterRequestDTO user
            ){
        logger.info("POST -> /register");
        // Register doesn't need auth

        // Check Permissions
        this.permissionService.checkRegisterUserPermission(user);

        this.userService.registerUser(user);
        return ResponseEntity.status(204).build();
    }

    @GetMapping
    @Operation(
            tags = "Get all users",
            description = "This operation provides support for retrieving all registered users accessible in the system, " +
                    "determined via the applied request authorization. By default, this call will return all matching records." +
                    "<br>" +
                    "The caller can define specific limits and pagination suited to their application by utilizing the " +
                    "`size` and `page` query parameters to restrict the quantity of the returned payload.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class))
                            )

                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request. Need a name param and the name cannot be empty",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized. Problems with authentication",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "The server encountered an unexpected error.",
                            content = @Content
                    ),

            }

    )
    public ResponseEntity<List<UserResponseDTO>> getAllByName(
            @RequestParam("name") String name,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "page", required= false) Integer page,
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String authHeader
    ){
        logger.info("GET -> /users");

        if(name.isEmpty()){
            throw new InvalidParamException("name", "Name cannot be empty");
        }

        // check authentication
        this.authService.authenticateUser(authHeader);

        List<UserResponseDTO> users = this.userService.getUsersByName(name, size, page);
        return ResponseEntity.ok(users);
    }



    @PutMapping("/{login}")
    @Operation(
            tags = "Update user profile",
            description = "This operation provides support for updating the details of an existing registered user in the system, " +
                    "determined via the applied request authorization. By default, this call will modify the user record based on the provided payload." +
                    "<br>" +
                    "The caller must specify the target user utilizing the `login` path parameter and provide the updated data within the request body." +
                    "<br>" +
                    "Note: While regular users are permitted to update their own profile information, modifying another user's record is strictly restricted to accounts with the `ADMIN` role.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "No Content",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Problems with authentication.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden. This user cannot change another user's profile.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found. Could not find a user with the provided email.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "The server encountered an unexpected error.",
                            content = @Content
                    ),

            }

    )
    public ResponseEntity<Void> updateUserRegister(
            @PathVariable String login,

            @Parameter(hidden = true) @RequestHeader(value = "Authorization")
            String authHeader,

            @Valid @RequestBody UserUpdateRequestDTO updateUser
    ){

        logger.info("PUT -> /{login}");
        // check authentication
        User authUser = this.authService.authenticateUser(authHeader);

        // Check permission
        this.permissionService.checkRegisterUpdatePermission(authUser, login);

        // Update User
        this.userService.updateUserRegister(updateUser, login);

        return ResponseEntity.status(204).build();
    }




    @PutMapping("/{login}/password")
    @Operation(
            tags = "Update Password",
            description = "This operation provides support for updating a user's password in our system, " +
                    "determined via the applied request authorization. By default, this call will modify the user password based on the provided payload." +
                    "<br>" +
                    "The caller must specify the target user utilizing the `login` path parameter and provide the new password within the request body." +
                    "<br>" +
                    "Note: This endpoint does not allow resetting another user's password.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "No Content",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Problems with authentication.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden. This user cannot change another user's password.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "The server encountered an unexpected error.",
                            content = @Content
                    ),

            }
    )
    public ResponseEntity<Void> updatePassword(
            @Valid
            @RequestBody UserResetPasswordRequestDTO password,

            @PathVariable String login,

            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String authHeader
            ){
        logger.info("UPDATE PASSWORD -> /{login}/password");

        // check authentication
        User authUser = this.authService.authenticateUser(authHeader);

        // check permissions
        this.permissionService.checkResetPasswordPermission(authUser, login);

        //Reset Password
        this.userService.resetUserPassword(authUser, password.password());

        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("/{login}")
    @Operation(
            tags = "Delete user profile",
            description = "This operation provides support for deleting an existing registered user in the system, " +
                    "determined via the applied request authorization. By default, this call will remove the user record based on the provided email. " +
                    "<br>" +
                    "The caller must specify the target user utilizing the `login` path parameter." +
                    "<br>" +
                    "Note: While regular users are permitted to delete their own account, deleting another user's account is strictly restricted to accounts with the `ADMIN` role.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "No Content",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Problems with authentication.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden. This user cannot change another user's profile.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found. Could not find a user with the provided email.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "The server encountered an unexpected error.",
                            content = @Content
                    ),

            }

    )
    public ResponseEntity<Void> deleteRegister(
            @PathVariable String login,

            @Parameter(hidden = true) @RequestHeader(value = "Authorization")
            String authHeader
    ){
        logger.info("DELETE -> /{login}");
        // check authentication
        User authUser = this.authService.authenticateUser(authHeader);

        // Check permission
        this.permissionService.checkDeleteUserPermission(authUser, login);

        // Delete User Data (register and address)
        this.userService.deleteUserRegister(login);

        return ResponseEntity.status(204).build();
    }


}
