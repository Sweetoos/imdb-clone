package org.example.imdbclone.dto;

public record TitleDTO(
        Integer titleId,
        String titleType,
        String primaryTitle,
        String originalTitle,
        Boolean isAdult,
        Integer startYear,
        Integer endYear,
        Integer runtimeMinutes
) {
}
