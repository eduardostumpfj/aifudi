package dev.aifudi.backend.dtos.response;

import dev.aifudi.backend.enums.RoleName;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "List of Users", title = "User Response Schema")
public record UserResponseDTO(

        @Schema(description = "Name of the user", example = "José da Silva", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @Schema(description = "Email of the user", example = "josedasilva@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @Schema(description = "Login of the user", example = "josedasilva@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
        String login,

        @Schema(description = "Name of the role", example = "OWNER", requiredMode = Schema.RequiredMode.REQUIRED, enumAsRef = true)
        RoleName roleName,

        @Schema(description = "CEP of the user", example = "80000-000", requiredMode = Schema.RequiredMode.REQUIRED)
        String cep,

        @Schema(description = "State Acronym", example = "PR", requiredMode = Schema.RequiredMode.REQUIRED)
        String state,

        @Schema(description = "Name of the city", example = "Curitiba", requiredMode = Schema.RequiredMode.REQUIRED)
        String city,

        @Schema(description = "Address of the user", example = "Rua XV de Novembro", requiredMode = Schema.RequiredMode.REQUIRED)
        String address,

        @Schema(description = "Number of the address", example = "111", requiredMode = Schema.RequiredMode.REQUIRED)
        String number,

        @Schema(description = "Complement of the address", example = "apt-202", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String complement
) {
}
