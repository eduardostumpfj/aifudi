package dev.aifudi.backend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserResetPasswordRequestDTO(
        @NotBlank
        @Size(max = 100, message = "The field must be 100 characters or less")
        String password
) {
}
