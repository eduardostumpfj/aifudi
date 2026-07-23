package dev.aifudi.backend.entities;

import jakarta.validation.constraints.Email;
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
public class User {
    private UUID id;

    @NotBlank
    private String name;

    @NotNull
    @Email
    private String email;

    @NotBlank
    private String hashedPassword;

    @NotNull
    private UUID roleId;
}
