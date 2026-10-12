package org.example.imdbclone.security;
import org.example.imdbclone.user.dto.UserResponseDto;
public record AuthResponse(String token, UserResponseDto user) {}
