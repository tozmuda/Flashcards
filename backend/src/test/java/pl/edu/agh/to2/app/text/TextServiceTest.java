package pl.edu.agh.to2.app.text;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TextServiceTest {
    @Mock
    private Parser parser;

    @InjectMocks
    private TextService textService;

    @Test
    void testParseText() {
        // Arrange
        String text = "Litwo ojczyzno moja";
        List<String> mockResponse = List.of("Litwo", "ojczyzno", "moja");

        when(parser.parse("Litwo ojczyzno moja")).thenReturn(mockResponse);

        // Act
        List<String> result = textService.parseText(text);

        // Assert
        assertEquals(mockResponse, result);
        verify(parser).parse("Litwo ojczyzno moja");
    }
}
