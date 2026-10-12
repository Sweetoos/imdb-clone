package org.example.imdbclone.review.dto;

import java.time.LocalDateTime;

public record ReviewResponseDto(
        Long reviewId,
        Long titleId,
        String titleName,
        Long userId,
        String username,
        Integer rating,
        String reviewText,
        LocalDateTime createdAt
) {
}
