package org.example.imdbclone.person;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.imdbclone.person.domain.Person;
import org.example.imdbclone.person.dto.PersonCreateDto;
import org.example.imdbclone.person.dto.PersonPatchDto;
import org.example.imdbclone.person.dto.PersonResponseDto;
import org.example.imdbclone.person.dto.PersonUpdateDto;
import org.example.imdbclone.person.exception.PersonNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    @Transactional
    public PersonResponseDto createPerson(PersonCreateDto dto) {
        boolean exists = personRepository.existsPerson(dto.firstName(), dto.lastName(), dto.birthDate(), dto.deathDate(), dto.role());
        if (exists) {
            throw new RuntimeException("Person already exists");
        }
        if (dto.deathDate() != null && dto.deathDate().isBefore(dto.birthDate())) {
            throw new RuntimeException("Death date cannot be before birth date");
        }

        Person personToSave = new Person();
        personToSave.setFirstName(dto.firstName());
        personToSave.setLastName(dto.lastName());
        personToSave.setBirthDate(dto.birthDate());
        personToSave.setDeathDate(dto.deathDate());
        personToSave.setRole(dto.role());

        Person savedPerson = personRepository.save(personToSave);
        return mapToResponseDto(savedPerson);
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

    public PersonResponseDto getPersonById(Long id) {
        Person person = personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
        return mapToResponseDto(person);
    }

    public List<PersonResponseDto> getAllPersons() {
        List<Person> persons = personRepository.findAll();
        return persons.stream().map(this::mapToResponseDto).toList();
    }

    @Transactional
    public PersonResponseDto updatePerson(Long id, PersonUpdateDto dto) {
        Person person = personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
        person.setFirstName(dto.firstName());
        person.setLastName(dto.lastName());
        person.setBirthDate(dto.birthDate());
        person.setDeathDate(dto.deathDate());
        person.setRole(dto.role());
        return mapToResponseDto(personRepository.save(person));
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
        return mapToResponseDto(personRepository.save(person));
    }

    @Transactional
    public void deletePerson(Long id) {
        boolean exists = personRepository.existsById(id);
        if (!exists) {
            throw new PersonNotFoundException(id);
        }
        personRepository.deleteById(id);
    }
}
