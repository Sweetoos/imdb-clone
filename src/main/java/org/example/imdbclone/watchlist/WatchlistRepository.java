package org.example.imdbclone.watchlist;

import org.example.imdbclone.watchlist.domain.Watchlist;
import org.example.imdbclone.watchlist.domain.WatchlistId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchlistRepository extends JpaRepository<Watchlist, WatchlistId> {
    List<Watchlist> findByUserUserId(Long userId);
    boolean existsById(WatchlistId id);
}