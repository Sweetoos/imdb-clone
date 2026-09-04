package org.example.imdbclone.genre.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GenreRequestDto(
        @NotBlank(message = "Genre name is required")
        @Size(max = 100, message = "Genre name must not exceed 100 characters")
        String genreName
) {
}
