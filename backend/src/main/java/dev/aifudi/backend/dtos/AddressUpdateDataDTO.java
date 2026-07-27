package dev.aifudi.backend.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddressUpdateDataDTO(
        @NotNull
        UUID userId,

        String cep,

        String state,

        String city,

        String address,

        String number,

        String complement

) {
}
