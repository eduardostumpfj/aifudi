package dev.aifudi.backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRegisterRequestDTO(
        @NotBlank
        String name,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String password,

        @NotBlank
        String roleName,

        @NotBlank
        String cep,

        @NotBlank
        String state,

        @NotBlank
        String city,

        @NotBlank
        String address,

        @NotBlank
        String number,

        String complement
) {
}
