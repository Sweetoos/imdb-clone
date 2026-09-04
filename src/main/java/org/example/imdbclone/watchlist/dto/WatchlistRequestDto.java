package org.example.imdbclone.watchlist.dto;

import jakarta.validation.constraints.NotNull;

public record WatchlistRequestDto(
        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Title ID is required")
        Long titleId
) {}