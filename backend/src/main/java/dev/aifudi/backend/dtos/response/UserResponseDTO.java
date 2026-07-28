package dev.aifudi.backend.dtos.response;

public record UserResponseDTO(
        String name,
        String email,
        String roleName,
        String cep,
        String state,
        String city,
        String address,
        String number,
        String complement
) {
}
