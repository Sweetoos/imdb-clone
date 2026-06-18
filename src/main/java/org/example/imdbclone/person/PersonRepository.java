package org.example.imdbclone.person;

import org.example.imdbclone.person.domain.Person;
import org.example.imdbclone.person.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

interface PersonRepository extends JpaRepository<Person,Long> {
    boolean existsByPersonCredentials(String firstName, String lastName, LocalDate birthDate, LocalDate deathDate, Role role);
}
