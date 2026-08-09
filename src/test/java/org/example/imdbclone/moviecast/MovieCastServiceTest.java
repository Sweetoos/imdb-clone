package org.example.imdbclone.moviecast;

import org.example.imdbclone.moviecast.domain.JobRole;
import org.example.imdbclone.moviecast.domain.MovieCast;
import org.example.imdbclone.moviecast.dto.MovieCastCreateDto;
import org.example.imdbclone.moviecast.dto.MovieCastResponseDto;
import org.example.imdbclone.person.PersonRepository;
import org.example.imdbclone.person.domain.Person;
import org.example.imdbclone.title.TitleRepository;
import org.example.imdbclone.title.domain.Title;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Nested
    @DisplayName("[MOVIE_CAST] SERVICE: CREATE")
    class CreateMovieCastTests{
        @Test
        @DisplayName("Should create movie cast successfully when title and person exist")
        void shouldCreateMovieCastSuccessfullyWhenTitleAndPersonExist() {
            MovieCastCreateDto createDto=new MovieCastCreateDto(1L, 10L,"Batman", JobRole.ACTOR);

            when(titleRepository.findById(1L)).thenReturn(Optional.of(sampleTitle));
            when(personRepository.findById(10L)).thenReturn(Optional.of(samplePerson));
            when(movieCastRepository.existsByTitleTitleIdAndPersonPersonIdAndJobRole(1L, 10L, JobRole.ACTOR)).thenReturn(false);
            when(movieCastRepository.save(any(MovieCast.class))).thenReturn(sampleMovieCast);

            MovieCastResponseDto result = movieCastService.createMovieCast(createDto);

            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(100L);
            assertThat(result.titleName()).isEqualTo("Batman");
            assertThat(result.personName()).isEqualTo("Leonardo DiCaprio");
            assertThat(result.characterName()).isEqualTo("Cobb");

            verify(movieCastRepository, times(1)).save(any(MovieCast.class));
        }
    }
}
