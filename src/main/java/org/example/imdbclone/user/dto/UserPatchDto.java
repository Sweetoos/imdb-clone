package org.example.imdbclone.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserPatchDto(
        @Size(min = 3, max = 50)
        String username,

        @Email
        String email
) {
}
