package pl.edu.agh.to2.app.exception;

import org.springframework.http.HttpStatus;

public class ApiRequestException extends RuntimeException {
    private final HttpStatus status;
    public ApiRequestException(String message) {
        super(message);
        status = HttpStatus.BAD_REQUEST;
    }

    public ApiRequestException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
