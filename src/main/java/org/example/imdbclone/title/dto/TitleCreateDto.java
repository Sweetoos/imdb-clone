package org.example.imdbclone.title.dto;

import org.example.imdbclone.title.domain.TitleType;

public record TitleCreateDto(
        String titleName,
        Boolean explicitContent,
        Integer runtimeMinutes,
        Integer startYear,
        Integer endYear,
        TitleType titleType
) {
}
