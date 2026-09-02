package org.example.imdbclone.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.example.imdbclone.moviecast.exception.MovieCastAlreadyExistsException;
import org.example.imdbclone.moviecast.exception.MovieCastNotFoundException;
import org.example.imdbclone.person.exception.PersonAlreadyExistsException;
import org.example.imdbclone.person.exception.PersonNotFoundException;
import org.example.imdbclone.review.exception.DuplicateReviewException;
import org.example.imdbclone.review.exception.ReviewNotFoundException;
import org.example.imdbclone.title.exception.TitleAlreadyExistsException;
import org.example.imdbclone.title.exception.TitleNotFoundException;
import org.example.imdbclone.user.exception.UserAlreadyExistsException;
import org.example.imdbclone.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 NOT_FOUND
    @ExceptionHandler({
            TitleNotFoundException.class,
            PersonNotFoundException.class,
            MovieCastNotFoundException.class,
            UserNotFoundException.class,
            ReviewNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(RuntimeException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // 409 CONFLICT
    @ExceptionHandler({
            TitleAlreadyExistsException.class,
            PersonAlreadyExistsException.class,
            MovieCastAlreadyExistsException.class,
            UserAlreadyExistsException.class,
            DuplicateReviewException.class
    })
    public ResponseEntity<ErrorResponse> handleConflictExceptions(RuntimeException ex, HttpServletRequest request){
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // 400 (arguments, date logic) and annotations with @Valid
    // 500
}
