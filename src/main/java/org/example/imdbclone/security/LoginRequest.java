package org.example.imdbclone.security;

public record LoginRequest(
        String username,
        String password
) {
}
