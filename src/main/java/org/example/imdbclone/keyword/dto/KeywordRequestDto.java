package org.example.imdbclone.keyword.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record KeywordRequestDto(
        @NotBlank(message = "Keyword name is required")
        @Size(max = 100, message = "Keyword name must not exceed 100 characters")
        String name
) {}