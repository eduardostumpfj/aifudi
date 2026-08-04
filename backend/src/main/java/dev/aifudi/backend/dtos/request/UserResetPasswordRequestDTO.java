package dev.aifudi.backend.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(title = "Update Password Schema")
public record UserResetPasswordRequestDTO(
        @NotBlank
        @Size(max = 100, message = "The field must be 100 characters or less")
        @Schema(description = "Password of the user", example = "jose123", requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {
}
