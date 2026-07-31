package dev.aifudi.backend.dtos.request;

import dev.aifudi.backend.enums.RoleName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterRequestDTO(
        @NotBlank
        @Size(max = 100, message = "The field must be 100 characters or less")
        String name,

        @NotBlank
        @Email
        @Size(max = 100, message = "The field must be 100 characters or less")
        String email,

        @NotBlank
        @Size(max = 100, message = "The field must be 100 characters or less")
        String password,

        @NotBlank
        @Size(max = 50, message = "The field must be 50 characters or less")
        RoleName roleName,

        @NotBlank
        @Size(max = 10, message = "The field must be 10 characters or less")
        String cep,

        @NotBlank
        @Size(max = 2, message = "The field must be 2 characters or less")
        String state,

        @NotBlank
        @Size(max = 100, message = "The field must be 100 characters or less")
        String city,

        @NotBlank
        @Size(max = 100, message = "The field must be 100 characters or less")
        String address,

        @NotBlank
        @Size(max = 10, message = "The field must be 10 characters or less")
        String number,

        @Size(max = 100, message = "The field must be 100 characters or less")
        String complement
) {
}
