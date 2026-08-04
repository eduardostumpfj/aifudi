package dev.aifudi.backend.dtos.request;

import dev.aifudi.backend.enums.RoleName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(title = "Register User Schema")
public record UserRegisterRequestDTO(
        @NotBlank
        @Size(max = 100, message = "The field must be 100 characters or less")
        @Schema(description = "Name of the user", example = "José da Silva", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @NotBlank
        @Email
        @Size(max = 100, message = "The field must be 100 characters or less")
        @Schema(description = "Email of the user", example = "josedasilva@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @NotBlank
        @Size(max = 100, message = "The field must be 100 characters or less")
        @Schema(description = "Password of the user", example = "jose123", requiredMode = Schema.RequiredMode.REQUIRED)
        String password,

        @NotNull(message = "Role name is required")
        @Schema(description = "Name of the role", example = "OWNER", requiredMode = Schema.RequiredMode.REQUIRED, enumAsRef = true)
        RoleName roleName,

        @NotBlank
        @Size(max = 10, message = "The field must be 10 characters or less")
        @Schema(description = "CEP of the user", example = "80000-000", requiredMode = Schema.RequiredMode.REQUIRED)
        String cep,

        @NotBlank
        @Size(max = 2, message = "The field must be 2 characters or less")
        @Schema(description = "State Acronym", example = "PR", requiredMode = Schema.RequiredMode.REQUIRED)
        String state,

        @NotBlank
        @Size(max = 100, message = "The field must be 100 characters or less")
        @Schema(description = "Name of the city", example = "Curitiba", requiredMode = Schema.RequiredMode.REQUIRED)
        String city,

        @NotBlank
        @Size(max = 100, message = "The field must be 100 characters or less")
        @Schema(description = "Address of the user", example = "Rua XV de Novembro", requiredMode = Schema.RequiredMode.REQUIRED)
        String address,

        @NotBlank
        @Size(max = 10, message = "The field must be 10 characters or less")
        @Schema(description = "Address of the user", example = "Rua XV de Novembro", requiredMode = Schema.RequiredMode.REQUIRED)
        String number,

        @Size(max = 100, message = "The field must be 100 characters or less")
        @Schema(description = "Complement of the address", example = "apt-202", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String complement
) {
}
