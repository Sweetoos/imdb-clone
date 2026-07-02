package org.example.imdbclone.title.dto;

import org.example.imdbclone.title.domain.Title;
import org.example.imdbclone.title.domain.TitleType;

public record TitleResponseDto(
        Long titleId,
        String titleName,
        Boolean explicitContent,
        Integer runtimeMinutes,
        Integer startYear,
        Integer endYear,
        TitleType titleType,
        Double averageRating
        //TODO: add actors
) {

}
