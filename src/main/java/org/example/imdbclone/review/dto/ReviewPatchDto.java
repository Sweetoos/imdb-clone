package org.example.imdbclone.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ReviewPatchDto(
        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 10, message = "Rating cannot exceed 10")
        Integer rating,
        String reviewText
) {
}
