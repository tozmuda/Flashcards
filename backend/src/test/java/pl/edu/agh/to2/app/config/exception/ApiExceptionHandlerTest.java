package pl.edu.agh.to2.app.config.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.edu.agh.to2.app.exception.ApiExceptionHandler;
import pl.edu.agh.to2.app.exception.ApiRequestException;
import pl.edu.agh.to2.app.exception.ErrorResponse;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiExceptionHandlerTest {
    @Mock
    ApiRequestException e;

    ApiExceptionHandler apiExceptionHandler;

    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    @BeforeEach
    void setUp() {
        apiExceptionHandler = new ApiExceptionHandler();
    }
    @Test
    void testHandleApiRequestException() {
        // Arrange
        when(e.getStatus()).thenReturn(httpStatus);
        when(e.getMessage()).thenReturn("test error message");

        // Act
        ResponseEntity<Object> response = apiExceptionHandler.handleApiRequestException(e);
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();

        // Assert
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.status());
        assertEquals("test error message", errorResponse.message());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}