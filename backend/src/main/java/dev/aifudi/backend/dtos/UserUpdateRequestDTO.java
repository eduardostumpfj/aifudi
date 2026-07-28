package dev.aifudi.backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDTO(
        @Email
        String email,

        String name,

        String roleName,

        String cep,

        String state,

        String city,

        String address,

        @Size(max = 10, message = "The field must be 10 characters or less")
        String number,

        String complement
) {
}
