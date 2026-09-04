package org.example.imdbclone.moviecast;

import org.example.imdbclone.moviecast.domain.JobRole;
import org.example.imdbclone.moviecast.domain.MovieCast;
import org.example.imdbclone.moviecast.dto.MovieCastCreateDto;
import org.example.imdbclone.moviecast.dto.MovieCastResponseDto;
import org.example.imdbclone.moviecast.dto.MovieCastUpdateDto;
import org.example.imdbclone.moviecast.exception.MovieCastNotFoundException;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        sampleTitle = Title.builder()
                .titleId(1L)
                .titleName("Batman")
                .build();

        samplePerson = Person.builder()
                .personId(10L)
                .firstName("Christian")
                .lastName("Bale")
                .build();

        sampleMovieCast = MovieCast.builder()
                .id(100L)
                .title(sampleTitle)
                .person(samplePerson)
                .characterName("Bruce Wayne")
                .jobRole(JobRole.ACTOR)
                .build();
    }

    @Nested
    @DisplayName("[MOVIE_CAST] SERVICE: CREATE")
    class CreateMovieCastTests {
        @Test
        @DisplayName("Should create movie cast successfully when title and person exist")
        void shouldCreateMovieCastSuccessfully() {
            MovieCastCreateDto createDto = new MovieCastCreateDto(1L, 10L, "Bruce Wayne", JobRole.ACTOR);

            when(titleRepository.findById(1L)).thenReturn(Optional.of(sampleTitle));
            when(personRepository.findById(10L)).thenReturn(Optional.of(samplePerson));
            when(movieCastRepository.existsByTitleTitleIdAndPersonPersonIdAndJobRole(1L, 10L, JobRole.ACTOR)).thenReturn(false);
            when(movieCastRepository.save(any(MovieCast.class))).thenReturn(sampleMovieCast);

            MovieCastResponseDto result = movieCastService.createMovieCast(createDto);

            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(100L);
            assertThat(result.titleName()).isEqualTo("Batman");
            assertThat(result.personName()).isEqualTo("Christian Bale");
            assertThat(result.characterName()).isEqualTo("Bruce Wayne");

            verify(movieCastRepository, times(1)).save(any(MovieCast.class));
        }

        @Test
        @DisplayName("Should throw RuntimeException when title does not exist")
        void shouldThrowExceptionWhenTitleNotFound() {
            MovieCastCreateDto createDto = new MovieCastCreateDto(1L, 10L, "Bruce Wayne", JobRole.ACTOR);
            when(titleRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> movieCastService.createMovieCast(createDto))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Title not found");
        }

        @Test
        @DisplayName("Should throw RuntimeException when cast already exists")
        void shouldThrowExceptionWhenCastAlreadyExists() {
            MovieCastCreateDto createDto = new MovieCastCreateDto(1L, 10L, "Bruce Wayne", JobRole.ACTOR);

            when(titleRepository.findById(1L)).thenReturn(Optional.of(sampleTitle));
            when(personRepository.findById(10L)).thenReturn(Optional.of(samplePerson));
            when(movieCastRepository.existsByTitleTitleIdAndPersonPersonIdAndJobRole(1L, 10L, JobRole.ACTOR)).thenReturn(true);

            assertThatThrownBy(() -> movieCastService.createMovieCast(createDto))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("already has cast");
        }
    }

    @Nested
    @DisplayName("[MOVIE_CAST] SERVICE: READ")
    class ReadMovieCastTests {
        @Test
        @DisplayName("Should return cast for a specific title")
        void shouldReturnCastForTitle() {
            when(movieCastRepository.findByTitleTitleId(1L)).thenReturn(List.of(sampleMovieCast));

            List<MovieCastResponseDto> result = movieCastService.getCastForTitle(1L);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).titleName()).isEqualTo("Batman");
            verify(movieCastRepository, times(1)).findByTitleTitleId(1L);
        }

        @Test
        @DisplayName("Should return filmography for a specific person")
        void shouldReturnFilmographyForPerson() {
            when(movieCastRepository.findByPersonPersonId(10L)).thenReturn(List.of(sampleMovieCast));

            List<MovieCastResponseDto> result = movieCastService.getFilmographyForPerson(10L);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).personName()).isEqualTo("Christian Bale");
            verify(movieCastRepository, times(1)).findByPersonPersonId(10L);
        }
    }

    @Nested
    @DisplayName("[MOVIE_CAST] SERVICE: UPDATE")
    class UpdateMovieCastTests {
        @Test
        @DisplayName("Should update cast character name and job role")
        void shouldUpdateCastSuccessfully() {
            Long castId = 100L;
            MovieCastUpdateDto updateDto = new MovieCastUpdateDto("Batman", JobRole.DIRECTOR);

            when(movieCastRepository.findById(castId)).thenReturn(Optional.of(sampleMovieCast));

            MovieCastResponseDto result = movieCastService.updateCast(castId, updateDto);

            assertThat(result.characterName()).isEqualTo("Batman");
            assertThat(result.jobRole()).isEqualTo(JobRole.DIRECTOR);
        }

        @Test
        @DisplayName("Should throw MovieCastNotFoundException when updating non-existing cast")
        void shouldThrowExceptionWhenUpdatingNonExistingCast() {
            Long nonExistingId = 999L;
            MovieCastUpdateDto updateDto = new MovieCastUpdateDto("Batman", JobRole.ACTOR);

            when(movieCastRepository.findById(nonExistingId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> movieCastService.updateCast(nonExistingId, updateDto))
                    .isInstanceOf(MovieCastNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("[MOVIE_CAST] SERVICE: DELETE")
    class DeleteMovieCastTests {
        @Test
        @DisplayName("Should delete cast successfully when it exists")
        void shouldDeleteCastSuccessfully() {
            Long castId = 100L;
            when(movieCastRepository.existsById(castId)).thenReturn(true);

            movieCastService.deleteCast(castId);

            verify(movieCastRepository, times(1)).deleteById(castId);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when deleting non-existing cast")
        void shouldThrowExceptionWhenDeletingNonExistingCast() {
            Long nonExistingId = 999L;
            when(movieCastRepository.existsById(nonExistingId)).thenReturn(false);

            assertThatThrownBy(() -> movieCastService.deleteCast(nonExistingId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("MovieCast not found");

            verify(movieCastRepository, never()).deleteById(anyLong());
        }
    }
}