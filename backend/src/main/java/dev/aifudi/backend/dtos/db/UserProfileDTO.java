package dev.aifudi.backend.dtos.db;


import java.util.UUID;

public record UserProfileDTO(
        String name,
        String email,
        String roleName,
        UUID addressId,
        String cep,
        String state,
        String city,
        String address,
        String number,
        String complement
) {
}
