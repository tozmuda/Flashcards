package pl.edu.agh.to2.app.filegenerator;

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.edu.agh.to2.app.config.AppConfig;
import pl.edu.agh.to2.app.deck.Deck;
import pl.edu.agh.to2.app.deck.flashcard.Flashcard;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
class DeckCSVCreatorServiceTest {

    @Mock
    private Deck deck;

    @Autowired
    private Charset charset;

    @Autowired
    private CSVFormat csvFormat;
    private DeckCSVCreatorService deckCSVCreatorService;

    @BeforeEach
    void setUp() {
        deckCSVCreatorService = new DeckCSVCreatorService(deck, charset, csvFormat);
    }

    @Test
    void testCreateCorrectCSV() throws IOException {
        // Arrange
        when(deck.getAllFlashcards()).thenReturn(List.of(
                new Flashcard("dogs", "pies", "dog",
                        "noun", "dog","dog"),
                new Flashcard("cats", "kot", "cat",
                        "noun", "cat","cat")
        ));
        String expectedCSV = "dogs,noun,dog,pies,dog,dog\r\ncats,noun,cat,kot,cat,cat\r\n";

        // Act
        byte[] csvData = deckCSVCreatorService.createCSV();

        // Assert
        assertArrayEquals(expectedCSV.getBytes(charset), csvData);
    }
}