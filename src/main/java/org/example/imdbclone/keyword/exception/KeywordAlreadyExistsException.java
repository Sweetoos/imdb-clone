package org.example.imdbclone.keyword.exception;

public class KeywordAlreadyExistsException extends RuntimeException {
    public KeywordAlreadyExistsException(String name) {
        super("Keyword already exists with name: " + name);
    }
}