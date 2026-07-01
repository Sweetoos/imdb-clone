package org.example.imdbclone.person;

import org.example.imdbclone.person.domain.Person;
import org.example.imdbclone.person.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

interface PersonRepository extends JpaRepository<Person, Long> {
    @Query("SELECT COUNT(p) > 0 FROM Person p WHERE p.firstName = :firstName AND p.lastName = :lastName AND p.birthDate = :birthDate AND p.deathDate = :deathDate AND p.role = :role")
    boolean existsPerson(String firstName, String lastName, LocalDate birthDate, LocalDate deathDate, Role role);
}
