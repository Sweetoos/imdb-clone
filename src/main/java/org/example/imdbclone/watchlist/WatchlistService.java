package org.example.imdbclone.watchlist;

import lombok.RequiredArgsConstructor;
import org.example.imdbclone.title.TitleRepository;
import org.example.imdbclone.title.domain.Title;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final UserRepository userRepository;
    private final TitleRepository titleRepository;

    @Transactional
    public WatchlistResponseDto addToWatchlist(WatchlistRequestDto dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new UserNotFoundException(dto.userId()));

        Title title = titleRepository.findById(dto.titleId())
                .orElseThrow(() -> new TitleNotFoundException(dto.titleId()));

        WatchlistId watchlistId = new WatchlistId(dto.userId(), dto.titleId());

        if (watchlistRepository.existsById(watchlistId)) {
            throw new WatchlistAlreadyExistsException("Title is already in user's watchlist");
        }

        Watchlist watchlist = Watchlist.builder()
                .id(watchlistId)
                .user(user)
                .title(title)
                .build();

        Watchlist saved = watchlistRepository.save(watchlist);
        return mapToDto(saved);
    }

    public List<WatchlistResponseDto> getUserWatchlist(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        return watchlistRepository.findByUserUserId(userId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional
    public void removeFromWatchlist(Long userId, Long titleId) {
        WatchlistId watchlistId = new WatchlistId(userId, titleId);

        if (!watchlistRepository.existsById(watchlistId)) {
            throw new WatchlistNotFoundException("Title not found in user's watchlist");
        }

        watchlistRepository.deleteById(watchlistId);
    }

    private WatchlistResponseDto mapToDto(Watchlist watchlist) {
        return new WatchlistResponseDto(
                watchlist.getUser().getUserId(),
                watchlist.getTitle().getTitleId(),
                watchlist.getTitle().getTitleName(),
                watchlist.getTitle().getStartYear(),
                watchlist.getTitle().getTitleType().name(),
                watchlist.getAddedAt()
        );
    }
}