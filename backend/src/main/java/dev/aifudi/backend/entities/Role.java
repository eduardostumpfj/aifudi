package dev.aifudi.backend.entities;

import dev.aifudi.backend.enums.RoleName;
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
public class Role {
    @NotNull
    private UUID id;

    @NotBlank
    private RoleName name;
}
