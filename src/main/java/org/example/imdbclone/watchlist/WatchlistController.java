package org.example.imdbclone.watchlist;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.imdbclone.watchlist.dto.WatchlistRequestDto;
import org.example.imdbclone.watchlist.dto.WatchlistResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlists")
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistService watchlistService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WatchlistResponseDto addToWatchlist(@RequestBody @Valid WatchlistRequestDto dto) {
        return watchlistService.addToWatchlist(dto);
    }

    @GetMapping("/user/{userId}")
    public List<WatchlistResponseDto> getUserWatchlist(@PathVariable Long userId) {
        return watchlistService.getUserWatchlist(userId);
    }

    @DeleteMapping("/user/{userId}/title/{titleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFromWatchlist(@PathVariable Long userId, @PathVariable Long titleId) {
        watchlistService.removeFromWatchlist(userId, titleId);
    }
}