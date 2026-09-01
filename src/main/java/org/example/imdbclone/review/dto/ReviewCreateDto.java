package org.example.imdbclone.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReviewCreateDto(
        @NotNull(message = "Title ID is required")
        Long titleId,

        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Rating is required")
        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 10, message = "Rating cannot exceed 10")
        Integer rating,

        String reviewText
) {
}
