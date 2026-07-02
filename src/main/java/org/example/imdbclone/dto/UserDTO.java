package org.example.imdbclone.dto;

import java.time.LocalDateTime;

public record UserDTO(
        Integer userId,
        String username,
        String email,
        LocalDateTime createdAt
) {
}
