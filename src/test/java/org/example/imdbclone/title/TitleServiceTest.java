package org.example.imdbclone.title;

import org.example.imdbclone.title.domain.Title;
import org.example.imdbclone.title.domain.TitleType;
import org.example.imdbclone.title.dto.TitleCreateDto;
import org.example.imdbclone.title.dto.TitlePatchDto;
import org.example.imdbclone.title.dto.TitleResponseDto;
import org.example.imdbclone.title.dto.TitleUpdateDto;
import org.example.imdbclone.title.exception.TitleNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TitleServiceTest {
    @Mock
    private TitleRepository titleRepository;

    @InjectMocks
    private TitleService titleService;

    private Title titleMockFromDb;

    @BeforeEach
    void setUp() {
        titleMockFromDb = new Title();
        titleMockFromDb.setTitleId(99999999L);
        titleMockFromDb.setTitleName("50 pozycji Lococka");
        titleMockFromDb.setExplicitContent(true);
        titleMockFromDb.setRuntimeMinutes(69);
        titleMockFromDb.setStartYear(2026);
        titleMockFromDb.setEndYear(2026);
        titleMockFromDb.setTitleType(TitleType.TVSERIES);
    }

    @Nested
    @DisplayName("[TITLE] SERVICE: CREATE")
    class CreateTitleTests {
        @Test
        @DisplayName("Should create title successfully when data is valid")
        void shouldCreateTitleSuccessfully() {
            TitleCreateDto createDto = new TitleCreateDto(
                    "50 pozycji Lococka",
                    true,
                    69,
                    2026,
                    2026,
                    TitleType.TVSERIES
            );

            when(titleRepository.save(any(Title.class))).thenReturn(titleMockFromDb);

            TitleResponseDto result = titleService.createTitle(createDto);

            assertThat(result).isNotNull();
            assertThat(result.titleId()).isEqualTo(99999999L);
            assertThat(result.titleName()).isEqualTo("50 pozycji Lococka");
            assertThat(result.runtimeMinutes()).isEqualTo(69);
            verify(titleRepository, times(1)).save(any(Title.class));
        }
    }

    @Nested
    @DisplayName("[TITLE] SERVICE: READ")
    class ReadTitleTests {

        @Test
        @DisplayName("Should return title DTO when title exists by ID")
        void shouldReturnTitleByIdSuccessfully() {
            Long existingId = 99999999L;
            when(titleRepository.findById(existingId)).thenReturn(Optional.of(titleMockFromDb));

            TitleResponseDto result = titleService.getTitleById(existingId);

            assertThat(result).isNotNull();
            assertThat(result.titleId()).isEqualTo(existingId);
            assertThat(result.titleName()).isEqualTo("50 pozycji Lococka");
        }

        @Test
        @DisplayName("Should return list of all titles")
        void shouldReturnAllTitles() {
            Title movie = new Title();
            movie.setTitleId(88888888888L);
            movie.setTitleName("Interstellar");
            movie.setExplicitContent(false);
            movie.setRuntimeMinutes(169);
            movie.setStartYear(2014);
            movie.setEndYear(2014);
            movie.setTitleType(TitleType.MOVIE);
            Title series = new Title();
            series.setTitleId(99999999999L);
            series.setTitleName("Game of Thrones");
            series.setExplicitContent(true);
            series.setStartYear(2011);
            series.setEndYear(2019);
            series.setTitleType(TitleType.TVSERIES);

            List<Title> titles = new ArrayList<>();
            titles.add(movie);
            titles.add(series);

            when(titleRepository.findAll()).thenReturn(titles);

            List<TitleResponseDto> result = titleService.getAllTitles();

            assertThat(result).isNotNull().hasSize(2);
            assertThat(result.get(0).titleId()).isEqualTo(88888888888L);
            assertThat(result.get(1).titleId()).isEqualTo(99999999999L);
            assertThat(result.get(0).titleName()).isEqualTo("Interstellar");
            assertThat(result.get(1).titleName()).isEqualTo("Game of Thrones");
            verify(titleRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should throw TitleNotFoundException when title does not exist")
        void shouldThrowExceptionWhenTitleNotFound() {
            Long nonExistingId = 11111111L;
            when(titleRepository.findById(nonExistingId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> titleService.getTitleById(nonExistingId))
                    .isInstanceOf(TitleNotFoundException.class)
                    .hasMessageContaining(String.valueOf(nonExistingId));

            verify(titleRepository, never()).save(any(Title.class));
        }
    }

    @Nested
    @DisplayName("[TITLE] SERVICE: UPDATE(PUT)")
    class UpdateTitleTests {
        @Test
        @DisplayName("Should update title successfully when data is valid")
        void shouldUpdateTitleSuccessfully() {
            Long existingId = 99999999L;
            TitleUpdateDto updateDto = new TitleUpdateDto(
                    "67 pozycji Lococka",
                    false,
                    67,
                    2027,
                    2028,
                    TitleType.MOVIE
            );

            when(titleRepository.findById(existingId)).thenReturn(Optional.of(titleMockFromDb));
            TitleResponseDto result = titleService.updateTitle(existingId, updateDto);

            assertThat(result).isNotNull();
            assertThat(result.titleName()).isEqualTo("67 pozycji Lococka");
            assertThat(result.explicitContent()).isEqualTo(false);
            assertThat(result.runtimeMinutes()).isEqualTo(67);
            assertThat(result.titleType()).isEqualTo(TitleType.MOVIE);
        }

        @Test
        @DisplayName("Should throw TitleNotFoundException when updating non-existing title")
        void shouldThrowExceptionWhenUpdatingNonExistingTitle() {
            Long nonExistingId = 11111111L;
            TitleUpdateDto updateDto = new TitleUpdateDto(
                    "Test",
                    false,
                    68,
                    2022,
                    2029,
                    TitleType.MOVIE
            );

            when(titleRepository.findById(nonExistingId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> titleService.updateTitle(nonExistingId, updateDto))
                    .isInstanceOf(TitleNotFoundException.class)
                    .hasMessageContaining(String.valueOf(nonExistingId));
        }
    }

    @Nested
    @DisplayName("[TITLE] SERVICE: PATCH")
    class PatchTitleTests {
        @Test
        @DisplayName("Should update only provided fields and ignore others")
        void shouldPatchTitleSuccessfully() {
            Long existingId = 99999999L;
            TitlePatchDto patchDto = new TitlePatchDto(
                    "2137 pozycji Lococka",
                    null,
                    2137,
                    null,
                    null,
                    null
            );

            when(titleRepository.findById(existingId)).thenReturn(Optional.of(titleMockFromDb));
            TitleResponseDto result = titleService.patchTitle(existingId, patchDto);

            assertThat(result).isNotNull();
            assertThat(result.titleName()).isEqualTo("2137 pozycji Lococka");
            assertThat(result.runtimeMinutes()).isEqualTo(2137);
            assertThat(result.explicitContent()).isEqualTo(true);
            assertThat(result.titleType()).isEqualTo(TitleType.TVSERIES);
        }
    }

    @Nested
    @DisplayName("[TITLE] SERVICE: DELETE")
    class DeleteTitleTests {
        @Test
        @DisplayName("Should delete title successfully")
        void shouldDeleteTitleSuccessfully() {
            Long existingId = 99999999L;
            when(titleRepository.existsById(existingId)).thenReturn(true);
            titleService.deleteTitle(existingId);
            verify(titleRepository, times(1)).deleteById(existingId);
        }

        @Test
        @DisplayName("Should throw TitleNotFoundException when title doesn't exist")
        void shouldThrowTitleNotFoundExceptionWhenTitleDoesntExist() {
            Long nonExistingId = 99999999L;
            when(titleRepository.existsById(nonExistingId)).thenReturn(false);
            assertThatThrownBy(() -> titleService.deleteTitle(nonExistingId))
                    .isInstanceOf(TitleNotFoundException.class)
                    .hasMessageContaining(String.valueOf(nonExistingId));

            verify(titleRepository, never()).deleteById(nonExistingId);
        }
    }
}
