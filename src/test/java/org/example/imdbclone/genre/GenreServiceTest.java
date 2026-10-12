package org.example.imdbclone.genre;

import org.example.imdbclone.genre.domain.Genre;
import org.example.imdbclone.genre.dto.GenreRequestDto;
import org.example.imdbclone.genre.dto.GenreResponseDto;
import org.example.imdbclone.genre.exception.GenreAlreadyExistsException;
import org.example.imdbclone.genre.exception.GenreNotFoundException;
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
public class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreService genreService;

    private Genre sampleGenre;

    @BeforeEach
    void setUp() {
        sampleGenre = Genre.builder()
                .genreId(1L)
                .genreName("Sci-Fi")
                .build();
    }

    @Nested
    @DisplayName("[GENRE] SERVICE: CREATE")
    class CreateGenreTests {

        @Test
        @DisplayName("Should create genre successfully when name is unique")
        void shouldCreateGenreSuccessfully() {
            GenreRequestDto requestDto = new GenreRequestDto("Sci-Fi");

            when(genreRepository.existsByGenreNameIgnoreCase("Sci-Fi")).thenReturn(false);
            when(genreRepository.save(any(Genre.class))).thenReturn(sampleGenre);

            GenreResponseDto result = genreService.createGenre(requestDto);

            assertThat(result).isNotNull();
            assertThat(result.genreId()).isEqualTo(1L);
            assertThat(result.genreName()).isEqualTo("Sci-Fi");
            verify(genreRepository, times(1)).save(any(Genre.class));
        }

        @Test
        @DisplayName("Should throw GenreAlreadyExistsException when genre name already exists")
        void shouldThrowExceptionWhenGenreNameAlreadyExists() {
            GenreRequestDto requestDto = new GenreRequestDto("Sci-Fi");
            when(genreRepository.existsByGenreNameIgnoreCase("Sci-Fi")).thenReturn(true);

            assertThatThrownBy(() -> genreService.createGenre(requestDto))
                    .isInstanceOf(GenreAlreadyExistsException.class)
                    .hasMessageContaining("Sci-Fi");

            verify(genreRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("[GENRE] SERVICE: READ")
    class ReadGenreTests {

        @Test
        @DisplayName("Should return genre when exists by ID")
        void shouldReturnGenreById() {
            when(genreRepository.findById(1L)).thenReturn(Optional.of(sampleGenre));

            GenreResponseDto result = genreService.getGenreById(1L);

            assertThat(result).isNotNull();
            assertThat(result.genreName()).isEqualTo("Sci-Fi");
        }

        @Test
        @DisplayName("Should throw GenreNotFoundException when genre does not exist")
        void shouldThrowExceptionWhenGenreNotFound() {
            when(genreRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> genreService.getGenreById(99L))
                    .isInstanceOf(GenreNotFoundException.class);
        }

        @Test
        @DisplayName("Should return all genres")
        void shouldReturnAllGenres() {
            Genre drama = Genre.builder().genreId(2L).genreName("Drama").build();
            when(genreRepository.findAll()).thenReturn(List.of(sampleGenre, drama));

            List<GenreResponseDto> result = genreService.getAllGenres();

            assertThat(result).hasSize(2);
            assertThat(result.get(0).genreName()).isEqualTo("Sci-Fi");
            assertThat(result.get(1).genreName()).isEqualTo("Drama");
        }
    }

    @Nested
    @DisplayName("[GENRE] SERVICE: UPDATE")
    class UpdateGenreTests {

        @Test
        @DisplayName("Should update genre name successfully")
        void shouldUpdateGenreSuccessfully() {
            GenreRequestDto updateDto = new GenreRequestDto("Science Fiction");
            when(genreRepository.findById(1L)).thenReturn(Optional.of(sampleGenre));
            when(genreRepository.existsByGenreNameIgnoreCase("Science Fiction")).thenReturn(false);

            GenreResponseDto result = genreService.updateGenre(1L, updateDto);

            assertThat(result).isNotNull();
            assertThat(result.genreName()).isEqualTo("Science Fiction");
        }
    }

    @Nested
    @DisplayName("[GENRE] SERVICE: DELETE")
    class DeleteGenreTests {

        @Test
        @DisplayName("Should delete genre successfully when exists")
        void shouldDeleteGenreSuccessfully() {
            when(genreRepository.existsById(1L)).thenReturn(true);

            genreService.deleteGenre(1L);

            verify(genreRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw GenreNotFoundException when deleting non-existing genre")
        void shouldThrowExceptionWhenDeletingNonExistingGenre() {
            when(genreRepository.existsById(99L)).thenReturn(false);

            assertThatThrownBy(() -> genreService.deleteGenre(99L))
                    .isInstanceOf(GenreNotFoundException.class);

            verify(genreRepository, never()).deleteById(any());
        }
    }
}