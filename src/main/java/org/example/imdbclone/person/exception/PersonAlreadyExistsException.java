package org.example.imdbclone.person.exception;

public class PersonAlreadyExistsException extends RuntimeException {
    public PersonAlreadyExistsException(String message) {
        super(message);
    }

    public PersonAlreadyExistsException(String firstName, String lastName) {
        super(String.format("Person '%s %s' already exists", firstName, lastName));
    }
}
