package org.example.imdbclone.person;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.imdbclone.person.domain.Person;
import org.example.imdbclone.person.dto.PersonCreateDto;
import org.example.imdbclone.person.dto.PersonResponseDto;
import org.example.imdbclone.person.exception.PersonNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    @Transactional
    public PersonResponseDto createPerson(PersonCreateDto dto){
        boolean exists=personRepository.existsByPersonCredentials(dto.firstName(),dto.lastName(),dto.birthDate(),dto.deathDate(),dto.role());
        if(exists){
            throw new RuntimeException("Person already exists");
        }
        if(dto.deathDate()!=null&&dto.deathDate().isBefore(dto.birthDate())){
            throw new RuntimeException("Death date cannot be before birth date");
        }

        Person personToSave=new Person();
        personToSave.setFirstName(dto.firstName());
        personToSave.setLastName(dto.lastName());
        personToSave.setBirthDate(dto.birthDate());
        personToSave.setDeathDate(dto.deathDate());
        personToSave.setRole(dto.role());

        Person savedPerson=personRepository.save(personToSave);
        return mapToResponseDto(savedPerson);
    }

    private PersonResponseDto mapToResponseDto(Person person){
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
        Person person=personRepository.findById(id).orElseThrow(()-> new PersonNotFoundException(id));
        return mapToResponseDto(person);
    }

    public List<PersonResponseDto> getAllPersons(){
        List<Person> persons=personRepository.findAll();
        return persons.stream().map(this::mapToResponseDto).toList();
    }
}
