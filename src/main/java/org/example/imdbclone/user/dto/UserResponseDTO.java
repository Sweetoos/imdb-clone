package org.example.imdbclone.user.dto;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long userId,
        String username,
        String email,
        LocalDateTime createdAt
) {
}
