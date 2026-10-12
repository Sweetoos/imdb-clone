package org.example.imdbclone.title.dto;

public record TitleHeaderDto(
        Long titleId,
        String titleType,
        Integer startYear,
        Double averageRating
) {
}
