package org.example.imdbclone.user.dto;

import java.time.LocalDateTime;

public record UserResponseDto(
        Long userId,
        String username,
        String email,
        LocalDateTime createdAt
) {
}
