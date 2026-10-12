package org.example.imdbclone.moviecast.exception;

public class MovieCastAlreadyExistsException extends RuntimeException {
    public MovieCastAlreadyExistsException(String message) {
        super(message);
    }
}
