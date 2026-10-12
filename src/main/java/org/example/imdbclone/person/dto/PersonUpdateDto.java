package org.example.imdbclone.person.dto;

import org.example.imdbclone.person.domain.Role;

import java.time.LocalDate;

public record PersonUpdateDto(
        String firstName,
        String lastName,
        LocalDate birthDate,
        LocalDate deathDate,
        Role role
) {
}
