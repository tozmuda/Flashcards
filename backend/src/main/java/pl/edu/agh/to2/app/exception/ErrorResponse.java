package pl.edu.agh.to2.app.exception;

import java.sql.Timestamp;

public record ErrorResponse(Timestamp timestamp, int status, String message) {
}
