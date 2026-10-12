package org.example.imdbclone.watchlist.exception;

public class WatchlistAlreadyExistsException extends RuntimeException {
    public WatchlistAlreadyExistsException(String message) {
        super(message);
    }
}