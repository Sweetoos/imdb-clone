package org.example.imdbclone.watchlist.dto;

import java.time.LocalDateTime;

public record WatchlistResponseDto(
        Long userId,
        Long titleId,
        String titleName,
        Integer startYear,
        String titleType,
        LocalDateTime addedAt
) {}