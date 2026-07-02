package org.example.imdbclone.common.exception;

import org.example.imdbclone.title.exception.TitleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public record ErrorResponse(
            String message,
            int status,
            LocalDateTime timestamp
    ) {
    }

    @ExceptionHandler(TitleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleTitleNotFoundException(TitleNotFoundException ex) {
        return new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
    }
}
