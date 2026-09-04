package org.example.imdbclone.watchlist.exception;

public class WatchlistNotFoundException extends RuntimeException {
    public WatchlistNotFoundException(String message) {
        super(message);
    }
}