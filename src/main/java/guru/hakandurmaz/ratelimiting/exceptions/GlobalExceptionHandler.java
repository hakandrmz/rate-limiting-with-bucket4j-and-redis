package guru.hakandurmaz.ratelimiting.exceptions;

import guru.hakandurmaz.ratelimiting.exceptions.TooManyRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({TooManyRequestException.class})
    public ResponseEntity<Object> handleTooManyRequestException(TooManyRequestException exception) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(exception.getMessage());
    }
}


