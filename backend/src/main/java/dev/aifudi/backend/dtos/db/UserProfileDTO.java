package dev.aifudi.backend.dtos.db;


import dev.aifudi.backend.enums.RoleName;

import java.util.UUID;

public record UserProfileDTO(
        UUID id,
        String name,
        String email,
        String login,
        RoleName roleName,
        String cep,
        String state,
        String city,
        String address,
        String number,
        String complement
) {
}
