package org.example.imdbclone.watchlist;

import org.example.imdbclone.title.TitleRepository;
import org.example.imdbclone.title.domain.Title;
import org.example.imdbclone.title.domain.TitleType;
import org.example.imdbclone.title.exception.TitleNotFoundException;
import org.example.imdbclone.user.UserRepository;
import org.example.imdbclone.user.domain.User;
import org.example.imdbclone.user.exception.UserNotFoundException;
import org.example.imdbclone.watchlist.domain.Watchlist;
import org.example.imdbclone.watchlist.domain.WatchlistId;
import org.example.imdbclone.watchlist.dto.WatchlistRequestDto;
import org.example.imdbclone.watchlist.dto.WatchlistResponseDto;
import org.example.imdbclone.watchlist.exception.WatchlistAlreadyExistsException;
import org.example.imdbclone.watchlist.exception.WatchlistNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WatchlistServiceTest {

    @Mock
    private WatchlistRepository watchlistRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TitleRepository titleRepository;

    @InjectMocks
    private WatchlistService watchlistService;

    private User sampleUser;
    private Title sampleTitle;
    private Watchlist sampleWatchlist;
    private WatchlistId sampleId;

    @BeforeEach
    void setUp() {
        sampleId = new WatchlistId(1L, 100L);

        sampleUser = User.builder()
                .userId(1L)
                .username("john_doe")
                .build();

        sampleTitle = Title.builder()
                .titleId(100L)
                .titleName("Inception")
                .startYear(2010)
                .titleType(TitleType.MOVIE)
                .build();

        sampleWatchlist = Watchlist.builder()
                .id(sampleId)
                .user(sampleUser)
                .title(sampleTitle)
                .addedAt(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("[WATCHLIST] SERVICE: ADD")
    class AddToWatchlistTests {

        @Test
        @DisplayName("Should add title to watchlist successfully")
        void shouldAddToWatchlistSuccessfully() {
            WatchlistRequestDto dto = new WatchlistRequestDto(1L, 100L);

            when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
            when(titleRepository.findById(100L)).thenReturn(Optional.of(sampleTitle));
            when(watchlistRepository.existsById(sampleId)).thenReturn(false);
            when(watchlistRepository.save(any(Watchlist.class))).thenReturn(sampleWatchlist);

            WatchlistResponseDto result = watchlistService.addToWatchlist(dto);

            assertThat(result).isNotNull();
            assertThat(result.userId()).isEqualTo(1L);
            assertThat(result.titleId()).isEqualTo(100L);
            assertThat(result.titleName()).isEqualTo("Inception");
            verify(watchlistRepository, times(1)).save(any(Watchlist.class));
        }

        @Test
        @DisplayName("Should throw WatchlistAlreadyExistsException when already added")
        void shouldThrowExceptionWhenAlreadyInWatchlist() {
            WatchlistRequestDto dto = new WatchlistRequestDto(1L, 100L);

            when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
            when(titleRepository.findById(100L)).thenReturn(Optional.of(sampleTitle));
            when(watchlistRepository.existsById(sampleId)).thenReturn(true);

            assertThatThrownBy(() -> watchlistService.addToWatchlist(dto))
                    .isInstanceOf(WatchlistAlreadyExistsException.class)
                    .hasMessageContaining("already in user's watchlist");

            verify(watchlistRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw UserNotFoundException when user does not exist")
        void shouldThrowExceptionWhenUserNotFound() {
            WatchlistRequestDto dto = new WatchlistRequestDto(99L, 100L);
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> watchlistService.addToWatchlist(dto))
                    .isInstanceOf(UserNotFoundException.class);

            verify(watchlistRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw TitleNotFoundException when title does not exist")
        void shouldThrowExceptionWhenTitleNotFound() {
            WatchlistRequestDto dto = new WatchlistRequestDto(1L, 999L);
            when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
            when(titleRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> watchlistService.addToWatchlist(dto))
                    .isInstanceOf(TitleNotFoundException.class);

            verify(watchlistRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("[WATCHLIST] SERVICE: GET")
    class GetWatchlistTests {

        @Test
        @DisplayName("Should return user's watchlist")
        void shouldReturnUserWatchlist() {
            when(userRepository.existsById(1L)).thenReturn(true);
            when(watchlistRepository.findByUserUserId(1L)).thenReturn(List.of(sampleWatchlist));

            List<WatchlistResponseDto> result = watchlistService.getUserWatchlist(1L);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).titleName()).isEqualTo("Inception");
        }
    }

    @Nested
    @DisplayName("[WATCHLIST] SERVICE: REMOVE")
    class RemoveFromWatchlistTests {

        @Test
        @DisplayName("Should remove title from watchlist successfully")
        void shouldRemoveFromWatchlistSuccessfully() {
            when(watchlistRepository.existsById(sampleId)).thenReturn(true);

            watchlistService.removeFromWatchlist(1L, 100L);

            verify(watchlistRepository, times(1)).deleteById(sampleId);
        }

        @Test
        @DisplayName("Should throw WatchlistNotFoundException when removing non-existing entry")
        void shouldThrowExceptionWhenRemovingNonExistingEntry() {
            when(watchlistRepository.existsById(sampleId)).thenReturn(false);

            assertThatThrownBy(() -> watchlistService.removeFromWatchlist(1L, 100L))
                    .isInstanceOf(WatchlistNotFoundException.class);

            verify(watchlistRepository, never()).deleteById(any());
        }
    }
}