package org.example.imdbclone.moviecast.dto;

import org.example.imdbclone.moviecast.domain.JobRole;

public record MovieCastCreateDto(
        Long titleId,
        Long personId,
        String characterName,
        JobRole jobRole
) {
}
