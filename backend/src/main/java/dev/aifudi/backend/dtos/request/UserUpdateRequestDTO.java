package dev.aifudi.backend.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDTO(
        @Email
        @Size(max = 100, message = "The field must be 100 characters or less")
        String email,

        @Size(max = 100, message = "The field must be 100 characters or less")
        String name,

        @Size(max = 50, message = "The field must be 50 characters or less")
        String roleName,

        @Size(max = 10, message = "The field must be 10 characters or less")
        String cep,

        @Size(max = 2, message = "The field must be 2 characters or less")
        String state,

        @Size(max = 100, message = "The field must be 100 characters or less")
        String city,

        @Size(max = 100, message = "The field must be 100 characters or less")
        String address,

        @Size(max = 10, message = "The field must be 10 characters or less")
        String number,

        @Size(max = 100, message = "The field must be 100 characters or less")
        String complement
) {
}
