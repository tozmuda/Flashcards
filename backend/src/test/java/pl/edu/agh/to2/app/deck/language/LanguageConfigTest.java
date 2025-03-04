package pl.edu.agh.to2.app.deck.language;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LanguageConfigTest {
    @Test
    void testLanguageConfigMapping() throws IOException {
        // Arrange
        String json = """
        {
           "languages": [
             {
               "name": "Polish",
               "short": "PL",
               "conjunction": "conjunction",
               "parts": [
                 {
                   "partOfSpeech": "noun",
                   "possiblePartsOfSentence": [
                     "subject",
                     "predicate noun",
                     "attribute"
                   ]
                 },
                 {
                   "partOfSpeech": "verb",
                   "possiblePartsOfSentence": [
                     "predicate",
                     "linking verb"
                   ]
                 }
               ]
             },
             {
               "name": "English",
               "short": "EN",
               "conjunction": "conjunction",
               "parts": [
                 {
                   "partOfSpeech": "noun",
                   "possiblePartsOfSentence": [
                     "subject",
                     "object",
                     "complement",
                     "subject complement"
                   ]
                 },
                 {
                   "partOfSpeech": "verb",
                   "possiblePartsOfSentence": [
                     "predicate",
                     "linking verb"
                   ]
                 }
               ]
             }
           ]
        }
        """;

        ObjectMapper objectMapper = new ObjectMapper();

        // Act
        LanguageConfig config = objectMapper.readValue(json, LanguageConfig.class);

        // Assert
        assertNotNull(config);
        assertEquals(2, config.getLanguages().size());

        Language language = config.getLanguages().getFirst();
        assertEquals("Polish", language.getName());
        assertEquals("PL", language.getShortCode());
        assertEquals("conjunction", language.getConjunction());
        assertEquals(2, language.getParts().size());

        Language.Part part = language.getParts().getFirst();
        assertEquals("noun", part.getPartOfSpeech());
        assertEquals(3, part.getPossiblePartsOfSentence().size());

        assertEquals(List.of("subject", "predicate noun", "attribute"), part.getPossiblePartsOfSentence());
    }
}
