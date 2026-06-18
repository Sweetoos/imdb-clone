package org.example.imdbclone.person;

import org.example.imdbclone.person.domain.Person;
import org.example.imdbclone.person.domain.Role;
import org.example.imdbclone.person.dto.PersonCreateDto;
import org.example.imdbclone.person.dto.PersonResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

            when(personRepository.save(any(Person.class))).thenReturn(personMockFromDb);
            PersonResponseDto result = personService.createPerson(createDto);

            assertThat(result).isNotNull();
            assertThat(result.personId()).isEqualTo(99999999L);
            assertThat(result.firstName()).isEqualTo("Mitch");
            assertThat(result.lastName()).isEqualTo("Lucker");
            assertThat(result.birthDate()).isEqualTo(LocalDate.of(1984, 10, 20));
            assertThat(result.deathDate()).isEqualTo(LocalDate.of(2012, 11, 1));
            assertThat(result.role()).isEqualTo(Role.DIRECTOR);

            verify(personRepository,times(1)).save(any(Person.class));
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

            PersonResponseDto result=personService.getPersonById(id);
        }

        @Test
        @DisplayName("Should return all persons")
        void shouldReturnAllPersons() {
            Person oli = new Person();
            oli.setPersonId(88888888888L);
            oli.setFirstName("Oli");
            oli.setLastName("Sykes");
            oli.setBirthDate(LocalDate.of(1986, 11, 20));
            oli.setRole(Role.ACTOR);

            Person noah=new Person();
            noah.setPersonId(99999999999L);
            noah.setFirstName("Noah");
            noah.setLastName("Sebastian");
            noah.setBirthDate(LocalDate.of(1995, 10, 31));
            noah.setRole(Role.WRITER);

            List<Person> persons=new ArrayList<>();
            persons.add(oli);
            persons.add(noah);

            when(personRepository.findAll()).thenReturn(persons);

            List<PersonResponseDto> result=personService.getAllPersons();

            assertNotNull(result);
            assertEquals(2,result.size());
            assertEquals(oli.getPersonId(),result.get(0).personId());
            assertEquals("Oli",result.get(0).firstName());
            assertEquals("Sykes",result.get(0).lastName());
            assertEquals(noah.getPersonId(),result.get(1).personId());
            assertEquals("Noah",result.get(1).firstName());
            assertEquals("Sebastian",result.get(1).lastName());
            verify(personRepository,times(1)).findAll();
        }
        @Test
        @DisplayName("Should throw PersonNotFoundException when person doesn't exist")
        void shouldThrowPersonNotFoundExceptionWhenPersonDoesntExist() {
        }
    }

}
