package org.example.imdbclone.title.exception;

public class TitleNotFoundException extends RuntimeException{
    public TitleNotFoundException(Long id) {
        super("Title with id " + id + " not found");
    }
}
