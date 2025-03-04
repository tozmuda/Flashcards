package pl.edu.agh.to2.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;
import java.time.Instant;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e) {
        Instant now = Instant.now();
        Timestamp timestamp = Timestamp.from(now);
        HttpStatus httpStatus = e.getStatus() != null ? e.getStatus() : HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse =  new ErrorResponse(
                timestamp,
                httpStatus.value(),
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
