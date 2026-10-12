package org.example.imdbclone.keyword;

import org.example.imdbclone.keyword.domain.Keyword;
import org.example.imdbclone.keyword.dto.KeywordRequestDto;
import org.example.imdbclone.keyword.dto.KeywordResponseDto;
import org.example.imdbclone.keyword.exception.KeywordAlreadyExistsException;
import org.example.imdbclone.keyword.exception.KeywordNotFoundException;
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
public class KeywordServiceTest {

    @Mock
    private KeywordRepository keywordRepository;

    @InjectMocks
    private KeywordService keywordService;

    private Keyword sampleKeyword;

    @BeforeEach
    void setUp() {
        sampleKeyword = Keyword.builder()
                .keywordId(1L)
                .name("dreams")
                .build();
    }

    @Nested
    @DisplayName("[KEYWORD] SERVICE: CREATE")
    class CreateKeywordTests {

        @Test
        @DisplayName("Should create keyword successfully when name is unique")
        void shouldCreateKeywordSuccessfully() {
            KeywordRequestDto requestDto = new KeywordRequestDto("dreams");

            when(keywordRepository.existsByNameIgnoreCase("dreams")).thenReturn(false);
            when(keywordRepository.save(any(Keyword.class))).thenReturn(sampleKeyword);

            KeywordResponseDto result = keywordService.createKeyword(requestDto);

            assertThat(result).isNotNull();
            assertThat(result.keywordId()).isEqualTo(1L);
            assertThat(result.name()).isEqualTo("dreams");
            verify(keywordRepository, times(1)).save(any(Keyword.class));
        }

        @Test
        @DisplayName("Should throw KeywordAlreadyExistsException when keyword already exists")
        void shouldThrowExceptionWhenKeywordAlreadyExists() {
            KeywordRequestDto requestDto = new KeywordRequestDto("dreams");
            when(keywordRepository.existsByNameIgnoreCase("dreams")).thenReturn(true);

            assertThatThrownBy(() -> keywordService.createKeyword(requestDto))
                    .isInstanceOf(KeywordAlreadyExistsException.class)
                    .hasMessageContaining("dreams");

            verify(keywordRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("[KEYWORD] SERVICE: READ")
    class ReadKeywordTests {

        @Test
        @DisplayName("Should return keyword when exists by ID")
        void shouldReturnKeywordById() {
            when(keywordRepository.findById(1L)).thenReturn(Optional.of(sampleKeyword));

            KeywordResponseDto result = keywordService.getKeywordById(1L);

            assertThat(result).isNotNull();
            assertThat(result.name()).isEqualTo("dreams");
        }

        @Test
        @DisplayName("Should throw KeywordNotFoundException when keyword does not exist")
        void shouldThrowExceptionWhenKeywordNotFound() {
            when(keywordRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> keywordService.getKeywordById(99L))
                    .isInstanceOf(KeywordNotFoundException.class);
        }

        @Test
        @DisplayName("Should return all keywords")
        void shouldReturnAllKeywords() {
            Keyword mafia = Keyword.builder().keywordId(2L).name("mafia").build();
            when(keywordRepository.findAll()).thenReturn(List.of(sampleKeyword, mafia));

            List<KeywordResponseDto> result = keywordService.getAllKeywords();

            assertThat(result).hasSize(2);
            assertThat(result.get(0).name()).isEqualTo("dreams");
            assertThat(result.get(1).name()).isEqualTo("mafia");
        }
    }

    @Nested
    @DisplayName("[KEYWORD] SERVICE: UPDATE")
    class UpdateKeywordTests {

        @Test
        @DisplayName("Should update keyword name successfully")
        void shouldUpdateKeywordSuccessfully() {
            KeywordRequestDto updateDto = new KeywordRequestDto("lucid dreams");
            when(keywordRepository.findById(1L)).thenReturn(Optional.of(sampleKeyword));
            when(keywordRepository.existsByNameIgnoreCase("lucid dreams")).thenReturn(false);

            KeywordResponseDto result = keywordService.updateKeyword(1L, updateDto);

            assertThat(result).isNotNull();
            assertThat(result.name()).isEqualTo("lucid dreams");
        }
    }

    @Nested
    @DisplayName("[KEYWORD] SERVICE: DELETE")
    class DeleteKeywordTests {

        @Test
        @DisplayName("Should delete keyword successfully when exists")
        void shouldDeleteKeywordSuccessfully() {
            when(keywordRepository.existsById(1L)).thenReturn(true);

            keywordService.deleteKeyword(1L);

            verify(keywordRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw KeywordNotFoundException when deleting non-existing keyword")
        void shouldThrowExceptionWhenDeletingNonExistingKeyword() {
            when(keywordRepository.existsById(99L)).thenReturn(false);

            assertThatThrownBy(() -> keywordService.deleteKeyword(99L))
                    .isInstanceOf(KeywordNotFoundException.class);

            verify(keywordRepository, never()).deleteById(any());
        }
    }
}