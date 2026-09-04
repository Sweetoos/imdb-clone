package org.example.imdbclone.moviecast.dto;

import org.example.imdbclone.moviecast.domain.JobRole;

public record MovieCastResponseDto(
        Long id,
        Long titleId,
        String titleName,
        Long personId,
        String personName,
        String characterName,
        JobRole jobRole
) {}
