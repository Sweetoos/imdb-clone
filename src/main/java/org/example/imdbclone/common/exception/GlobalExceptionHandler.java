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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request){
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> validationErrors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed for one or more fields",
                request.getRequestURI(),
                validationErrors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
