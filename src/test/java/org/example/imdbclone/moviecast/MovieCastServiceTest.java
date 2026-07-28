package org.example.imdbclone.moviecast;

import org.example.imdbclone.moviecast.domain.JobRole;
import org.example.imdbclone.moviecast.domain.MovieCast;
import org.example.imdbclone.moviecast.dto.MovieCastCreateDto;
import org.example.imdbclone.person.PersonRepository;
import org.example.imdbclone.person.PersonService;
import org.example.imdbclone.person.domain.Person;
import org.example.imdbclone.title.TitleRepository;
import org.example.imdbclone.title.TitleService;
import org.example.imdbclone.title.domain.Title;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MovieCastServiceTest {
    @Mock
    private MovieCastRepository movieCastRepository;
    @Mock
    private TitleRepository titleRepository;
    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private MovieCastService movieCastService;

    private MovieCast sampleMovieCast;
    private Title sampleTitle;
    private Person samplePerson;

    @BeforeEach
    void setUp() {
        sampleTitle=Title.builder()
                .titleId(1L)
                .titleName("Batman")
                .build();

        samplePerson=Person.builder()
                .personId(10L)
                .firstName("Christian")
                .lastName("Bale")
                .build();

        sampleMovieCast= MovieCast.builder()
                .id(1L)
                .title(sampleTitle)
                .person(samplePerson)
                .characterName("Batman")
                .build();
    }

    @Test
    @DisplayName("Should create movie cast successfully when title and person exist")
    void shouldCreateMovieCastSuccessfullyWhenTitleAndPersonExist() {
        MovieCastCreateDto createDto=new MovieCastCreateDto(1L, 10L,"Batman", JobRole.ACTOR);
    }
}
