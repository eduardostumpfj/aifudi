package dev.aifudi.backend.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Roles")
public enum RoleName {
    ADMIN,
    OWNER,
    USER
}
