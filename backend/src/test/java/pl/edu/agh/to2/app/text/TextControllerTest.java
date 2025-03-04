package pl.edu.agh.to2.app.text;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import pl.edu.agh.to2.app.dto.TextDTO;
import pl.edu.agh.to2.app.exception.ApiRequestException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TextControllerTest {
    @Mock
    private TextService textService;

    @InjectMocks
    private TextController textController;

    @Test
    void testParseText() {
        // Arrange
        TextDTO textDTO = new TextDTO("Litwo ojczyzno moja");
        List<String> mockResponse = List.of("Litwo", "ojczyzno", "moja");

        when(textService.parseText(textDTO.text())).thenReturn(mockResponse);

        // Act
        ResponseEntity<List<String>> response = textController.parseText(textDTO);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void testParseTextWithEmptyResult() {
        // Arrange
        TextDTO textDTO = new TextDTO(",;,:");
        List<String> mockResponse = List.of();

        when(textService.parseText(textDTO.text())).thenReturn(mockResponse);

        // Assert
        assertThrows(ApiRequestException.class, () -> textController.parseText(textDTO));
    }
}
