package org.example.imdbclone.watchlist;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("@ownership.user(authentication, #p0.userId())")
    public WatchlistResponseDto addToWatchlist(@RequestBody @Valid WatchlistRequestDto dto) {
        return watchlistService.addToWatchlist(dto);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("@ownership.user(authentication, #p0)")
    public List<WatchlistResponseDto> getUserWatchlist(@PathVariable Long userId) {
        return watchlistService.getUserWatchlist(userId);
    }

    @DeleteMapping("/user/{userId}/title/{titleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@ownership.user(authentication, #p0)")
    public void removeFromWatchlist(@PathVariable Long userId, @PathVariable Long titleId) {
        watchlistService.removeFromWatchlist(userId, titleId);
    }
}