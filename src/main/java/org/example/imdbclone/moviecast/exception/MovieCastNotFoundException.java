package org.example.imdbclone.moviecast.exception;

public class MovieCastNotFoundException extends RuntimeException {
    public MovieCastNotFoundException(Long id) {
        super("Movie cast with id " + id + " not found");
    }
}
