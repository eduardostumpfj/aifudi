package dev.aifudi.backend.dtos;

import org.springframework.http.HttpStatus;

public record ErrorDTO(
        String message,
        int status
) {
}
