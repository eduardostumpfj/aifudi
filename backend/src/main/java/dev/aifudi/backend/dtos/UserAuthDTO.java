package dev.aifudi.backend.dtos;

import jakarta.validation.constraints.NotBlank;

public record UserAuthDTO(
        @NotBlank
        String email,

        @NotBlank
        String password
) {
}
