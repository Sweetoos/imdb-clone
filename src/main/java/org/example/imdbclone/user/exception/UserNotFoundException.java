package org.example.imdbclone.user.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found with id: "+id);
    }

    public UserNotFoundException(String message) {
        super("User not found with identifier: "+message);
    }
}
