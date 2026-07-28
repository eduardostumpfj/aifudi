package dev.aifudi.backend.dtos.db;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserUpdateDataDTO(
        @NotNull
        UUID id,

        String name,

        String email,

        UUID roleId
) {
}
