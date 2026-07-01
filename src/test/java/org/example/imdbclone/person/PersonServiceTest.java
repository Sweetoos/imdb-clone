package org.example.imdbclone.person;

import org.example.imdbclone.person.domain.Person;
import org.example.imdbclone.person.domain.Role;
import org.example.imdbclone.person.dto.PersonCreateDto;
import org.example.imdbclone.person.dto.PersonPatchDto;
import org.example.imdbclone.person.dto.PersonResponseDto;
import org.example.imdbclone.person.dto.PersonUpdateDto;
import org.example.imdbclone.person.exception.PersonNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {
    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    private Person personMockFromDb;

    @BeforeEach
    void setUp() {
        personMockFromDb = new Person();
        personMockFromDb.setPersonId(99999999L);
        personMockFromDb.setFirstName("Mitch");
        personMockFromDb.setLastName("Lucker");
        personMockFromDb.setBirthDate(LocalDate.of(1984, 10, 20));
        personMockFromDb.setDeathDate(LocalDate.of(2012, 11, 1));
        personMockFromDb.setRole(Role.DIRECTOR);
    }

    @Nested
    @DisplayName("[PERSON] SERVICE: CREATE")
    class CreatePersonTests {
        @Test
        @DisplayName("Should create a person")
        void shouldCreatePersonSuccessfully() {
            PersonCreateDto createDto = new PersonCreateDto("Mitch", "Lucker", LocalDate.of(1984, 10, 20), LocalDate.of(2012, 11, 1), Role.DIRECTOR);

            when(personRepository.existsPerson(any(), any(), any(), any(), any())).thenReturn(false);
            when(personRepository.save(any(Person.class))).thenReturn(personMockFromDb);
            PersonResponseDto result = personService.createPerson(createDto);

            assertThat(result).isNotNull();
            assertThat(result.personId()).isEqualTo(99999999L);
            assertThat(result.firstName()).isEqualTo("Mitch");
            assertThat(result.lastName()).isEqualTo("Lucker");
            assertThat(result.birthDate()).isEqualTo(LocalDate.of(1984, 10, 20));
            assertThat(result.deathDate()).isEqualTo(LocalDate.of(2012, 11, 1));
            assertThat(result.role()).isEqualTo(Role.DIRECTOR);

            verify(personRepository, times(1)).save(any(Person.class));
        }
    }

    @Nested
    @DisplayName("[PERSON] SERVICE: READ")
    class ReadPersonTests {
        @Test
        @DisplayName("Should return person DTO when person exists by ID")
        void shouldReturnPersonByIdSuccessfully() {
            Long id = 99999999L;
            when(personRepository.findById(id)).thenReturn(Optional.of(personMockFromDb));

            PersonResponseDto result = personService.getPersonById(id);
        }

        @Test
        @DisplayName("Should return all persons")
        void shouldReturnAllPersons() {
            Person oli = new Person();
            oli.setPersonId(88888888L);
            oli.setFirstName("Oli");
            oli.setLastName("Sykes");
            oli.setBirthDate(LocalDate.of(1986, 11, 20));
            oli.setDeathDate(null);
            oli.setRole(Role.ACTOR);

            Person noah = new Person();
            noah.setPersonId(99999999L);
            noah.setFirstName("Noah");
            noah.setLastName("Sebastian");
            noah.setBirthDate(LocalDate.of(1995, 10, 31));
            noah.setDeathDate(null);
            noah.setRole(Role.WRITER);

            List<Person> persons = new ArrayList<>();
            persons.add(oli);
            persons.add(noah);

            when(personRepository.findAll()).thenReturn(persons);

            List<PersonResponseDto> result = personService.getAllPersons();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(oli.getPersonId(), result.get(0).personId());
            assertEquals("Oli", result.get(0).firstName());
            assertEquals("Sykes", result.get(0).lastName());
            assertEquals(noah.getPersonId(), result.get(1).personId());
            assertEquals("Noah", result.get(1).firstName());
            assertEquals("Sebastian", result.get(1).lastName());
            verify(personRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should throw PersonNotFoundException when person doesn't exist")
        void shouldThrowPersonNotFoundExceptionWhenPersonDoesntExist() {
            Long nonExistingId = 99999999L;
            when(personRepository.findById(nonExistingId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> personService
                    .getPersonById(nonExistingId))
                    .isInstanceOf(PersonNotFoundException.class).hasMessageContaining(String.valueOf(nonExistingId));

            verify(personRepository, never()).save(any(Person.class));
        }
    }

    @Nested
    @DisplayName("[PERSON] SERVICE: UPDATE")
    class UpdatePersonTests {
        @Test
        @DisplayName("Should update a person successfully when data is valid")
        void shouldUpdatePersonSuccessfully() {
            Long id = 99999999L;
            PersonUpdateDto updateDto = new PersonUpdateDto(
                    "Courtney",
                    "LaPlante",
                    LocalDate.of(1989, 1, 26),
                    null,
                    Role.DIRECTOR);

            when(personRepository.findById(id)).thenReturn(Optional.of(personMockFromDb));
            when(personRepository.save(any(Person.class))).thenReturn(personMockFromDb);
            PersonResponseDto result = personService.updatePerson(id, updateDto);

            assertThat(result).isNotNull();
            assertThat(result.personId()).isEqualTo(99999999L);
            assertThat(result.firstName()).isEqualTo("Courtney");
            assertThat(result.lastName()).isEqualTo("LaPlante");
            assertThat(result.birthDate()).isEqualTo(LocalDate.of(1989, 1, 26));
            assertThat(result.deathDate()).isNull();
            assertThat(result.role()).isEqualTo(Role.DIRECTOR);
        }

        @Test
        @DisplayName("Should throw PersonNotFoundException when updating non-existing person")
        void shouldThrowPersonNotFoundExceptionWhenUpdatingNonExistingPerson() {
            Long nonExistingId = 99999999L;
            PersonUpdateDto updateDto=new PersonUpdateDto(
                    "Ozzy",
                    "Osbourne",
                    LocalDate.of(1948,11,3),
                    LocalDate.of(2025,6,22),
                    Role.WRITER
            );

            when(personRepository.findById(nonExistingId)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> personService.updatePerson(nonExistingId, updateDto))
                    .isInstanceOf(PersonNotFoundException.class)
                    .hasMessageContaining(String.valueOf(nonExistingId));
        }
    }

    @Nested
    @DisplayName("[PERSON] SERVICE: PATCH")
    class PatchPersonTests {
        @Test
        @DisplayName("Should patch only provided fields and ignore others")
        void shouldPatchTitleSuccessfully(){
            Long existingId = 99999999L;
            PersonPatchDto updateDto = new PersonPatchDto(
                    "Max",
                    null,
                    null,
                    null,
                    Role.WRITER
            );
            when(personRepository.findById(existingId)).thenReturn(Optional.of(personMockFromDb));
            when(personRepository.save(any(Person.class))).thenReturn(personMockFromDb);
            PersonResponseDto result = personService.patchPerson(existingId, updateDto);

            assertThat(result).isNotNull();
            assertThat(result.personId()).isEqualTo(existingId);
            assertThat(result.firstName()).isEqualTo("Max");
            assertThat(result.role()).isEqualTo(Role.WRITER);
        }
    }

    @Nested
    @DisplayName("[PERSON] SERVICE: DELETE")
    class DeletePersonTests {
        @Test
        @DisplayName("Should delete person successfully")
        void shouldDeletePersonSuccessfully() {
            Long existingId = 99999999L;
            when(personRepository.existsById(existingId)).thenReturn(true);
            personService.deletePerson(existingId);
            verify(personRepository, times(1)).deleteById(existingId);
        }
        @Test
        @DisplayName("Should throw PersonNotFoundException when person doesn't exist")
        void shouldThrowPersonNotFoundExceptionWhenPersonDoesntExist() {
            Long nonExistingId = 99999999L;

            when(personRepository.existsById(nonExistingId)).thenReturn(false);
            assertThatThrownBy(() -> personService.deletePerson(nonExistingId))
                    .isInstanceOf(PersonNotFoundException.class);
            verify(personRepository, never()).deleteById(any());
        }
    }

}
