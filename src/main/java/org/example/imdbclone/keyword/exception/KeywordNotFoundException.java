package org.example.imdbclone.keyword.exception;

public class KeywordNotFoundException extends RuntimeException {
    public KeywordNotFoundException(Long id) {
        super("Keyword not found with id: " + id);
    }
}