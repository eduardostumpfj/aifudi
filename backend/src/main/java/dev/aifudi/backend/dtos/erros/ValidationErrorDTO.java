package dev.aifudi.backend.dtos.erros;

import java.util.List;

public record ValidationErrorDTO (
        List<String> errors,
        int status
) {
}
