package org.example.imdbclone.person;

import lombok.RequiredArgsConstructor;
import org.example.imdbclone.person.domain.Person;
import org.example.imdbclone.person.dto.PersonCreateDto;
import org.example.imdbclone.person.dto.PersonPatchDto;
import org.example.imdbclone.person.dto.PersonResponseDto;
import org.example.imdbclone.person.dto.PersonUpdateDto;
import org.example.imdbclone.person.exception.PersonAlreadyExistsException;
import org.example.imdbclone.person.exception.PersonNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;

    @Transactional
    public PersonResponseDto createPerson(PersonCreateDto dto) {
        boolean exists = personRepository.existsPerson(
                dto.firstName(), dto.lastName(), dto.birthDate(), dto.deathDate(), dto.role());
        if (exists) {
            throw new PersonAlreadyExistsException("Person already exists");
        }

        validateDates(dto.birthDate(), dto.deathDate());

        Person personToSave = Person.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .birthDate(dto.birthDate())
                .deathDate(dto.deathDate())
                .role(dto.role())
                .build();

        Person savedPerson = personRepository.save(personToSave);
        return mapToResponseDto(savedPerson);
    }

    public PersonResponseDto getPersonById(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
        return mapToResponseDto(person);
    }

    public List<PersonResponseDto> getAllPersons() {
        return personRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Transactional
    public PersonResponseDto updatePerson(Long id, PersonUpdateDto dto) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));

        validateDates(dto.birthDate(), dto.deathDate());

        person.setFirstName(dto.firstName());
        person.setLastName(dto.lastName());
        person.setBirthDate(dto.birthDate());
        person.setDeathDate(dto.deathDate());
        person.setRole(dto.role());

        return mapToResponseDto(person);
    }

    @Transactional
    public PersonResponseDto patchPerson(Long id, PersonPatchDto dto) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));

        Optional.ofNullable(dto.firstName()).ifPresent(person::setFirstName);
        Optional.ofNullable(dto.lastName()).ifPresent(person::setLastName);
        Optional.ofNullable(dto.birthDate()).ifPresent(person::setBirthDate);
        Optional.ofNullable(dto.deathDate()).ifPresent(person::setDeathDate);
        Optional.ofNullable(dto.role()).ifPresent(person::setRole);

        validateDates(person.getBirthDate(), person.getDeathDate());

        return mapToResponseDto(person);
    }

    @Transactional
    public void deletePerson(Long id) {
        if (!personRepository.existsById(id)) {
            throw new PersonNotFoundException(id);
        }
        personRepository.deleteById(id);
    }

    private void validateDates(LocalDate birthDate, LocalDate deathDate) {
        if (birthDate != null && deathDate != null && deathDate.isBefore(birthDate)) {
            throw new IllegalArgumentException("Death date cannot be before birth date");
        }
    }

    private PersonResponseDto mapToResponseDto(Person person) {
        return new PersonResponseDto(
                person.getPersonId(),
                person.getFirstName(),
                person.getLastName(),
                person.getBirthDate(),
                person.getDeathDate(),
                person.getRole()
        );
    }
}