package dev.aifudi.backend.dtos.request;

import dev.aifudi.backend.enums.RoleName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(title = "Update Profile Schema")
public record UserUpdateRequestDTO(
        @Schema(description = "Email of the user", example = "josedasilva@gmail.com", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Email
        @Size(max = 100, message = "The field must be 100 characters or less")
        String email,

        @Schema(description = "Name of the user", example = "José da Silva", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Size(max = 100, message = "The field must be 100 characters or less")
        String name,

        @Schema(description = "CEP of the user", example = "80000-000", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Size(max = 10, message = "The field must be 10 characters or less")
        String cep,

        @Schema(description = "State Acronym", example = "PR", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Size(max = 2, message = "The field must be 2 characters or less")
        String state,

        @Schema(description = "Name of the city", example = "Curitiba", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Size(max = 100, message = "The field must be 100 characters or less")
        String city,

        @Schema(description = "Address of the user", example = "Rua XV de Novembro", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Size(max = 100, message = "The field must be 100 characters or less")
        String address,

        @Schema(description = "Number of the address", example = "111", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Size(max = 10, message = "The field must be 10 characters or less")
        String number,

        @Schema(description = "Complement of the address", example = "apt-202", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Size(max = 100, message = "The field must be 100 characters or less")
        String complement
) {
}
