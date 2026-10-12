package org.example.imdbclone.title.dto;

import org.example.imdbclone.moviecast.domain.JobRole;
import org.example.imdbclone.title.domain.TitleType;

import java.util.List;

public record TitleResponseDto(
        Long titleId,
        String titleName,
        Boolean explicitContent,
        Integer runtimeMinutes,
        Integer startYear,
        Integer endYear,
        TitleType titleType,
        Double averageRating,
        Integer numVotes,
        List<String> genres,
        List<String> keywords,
        List<CastMemberDto> cast
) {
    public record CastMemberDto(
            Long personId,
            String personName,
            String characterName,
            JobRole jobRole
    ) {}
}