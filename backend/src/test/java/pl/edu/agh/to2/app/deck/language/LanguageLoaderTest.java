package pl.edu.agh.to2.app.deck.language;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LanguageLoaderTest {

    @Test
    void testLoadConfig() throws IOException {
        LanguageLoader loader = new LanguageLoader();

        // Act
        LanguageConfig config = loader.getConfig();

        // Assert
        assertNotNull(config, "Config should not be null");
    }
}
