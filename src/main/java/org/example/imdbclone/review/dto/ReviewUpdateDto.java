package org.example.imdbclone.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReviewUpdateDto(
        @NotNull(message = "Rating is required")
        @Min(value = 1, message = "Rating cannot exceed 10")
        @Max(value = 10, message = "Rating must be at least 1")
        Integer rating,

        String reviewText

) {
}
