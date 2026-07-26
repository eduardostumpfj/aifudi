package dev.aifudi.backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequestDTO(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String password,

        String name,

        String roleName,

        String cep,

        String state,

        String city,

        String address,

        String number,

        String complement
) {
}
