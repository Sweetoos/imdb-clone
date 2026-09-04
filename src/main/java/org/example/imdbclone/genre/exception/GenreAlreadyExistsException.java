package org.example.imdbclone.genre.exception;

public class GenreAlreadyExistsException extends RuntimeException {
    public GenreAlreadyExistsException(String name) {
        super("Genre already exists with name: " + name);
    }
}
