package dev.aifudi.backend.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Address {
    private UUID id;

    @NotNull
    private UUID userId;

    @NotBlank
    private String cep;

    @NotBlank
    private String state;

    @NotBlank
    private String city;

    @NotBlank
    private String address;

    @NotBlank
    private String number;

    private String complement;

}
