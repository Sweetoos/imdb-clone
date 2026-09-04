package org.example.imdbclone.moviecast.dto;

import org.example.imdbclone.moviecast.domain.JobRole;

public record MovieCastUpdateDto(
        String characterName,
        JobRole jobRole
) {
}
