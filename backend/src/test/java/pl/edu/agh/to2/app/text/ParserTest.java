package pl.edu.agh.to2.app.text;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.to2.app.deck.Deck;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ParserTest {

    @Mock
    private Deck deck;

    @InjectMocks
    Parser parser;


    @ParameterizedTest
    @ValueSource(strings = {"Hello there! How are you?",
            "       Hello      there!   !   How     are    you?       ?     ",
            "4363Hello4564there! 5465How are5656 you?66"})
    void testParseEnglishText(String text) {
        // Arrange
        List<String> expected = List.of("hello", "there", "how", "are", "you");

        // Act
        List<String> actual = parser.parse(text);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testParsePolishText() {
        // Arrange
        String text = "Cześć! Jak się masz?";
        List<String> expected = List.of("cześć", "jak", "się", "masz");

        // Act
        List<String> actual = parser.parse(text);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testParseGreekText() {
        // Arrange
        String text = "Γεια! Τι κάνετε;";
        List<String> expected = List.of("γεια", "τι", "κάνετε");

        // Act
        List<String> actual = parser.parse(text);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testParseArabicText() {
        // Arrange
        String text = "ماذا يحدث معك هناك؟";
        List<String> expected = List.of("ماذا", "يحدث", "معك", "هناك");

        // Act
        List<String> actual = parser.parse(text);

        // Assert
        assertEquals(expected, actual);
    }


    @ParameterizedTest
    @MethodSource("provideTestCases")
    void testParser(String input, List<String> expected) {
        // Act
        List<String> actual = parser.parse(input);

        // Assert
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of("", List.of()),
                Arguments.of(null, List.of()),
                Arguments.of("!@#$%^&*()_+-=<>?", List.of())
        );
    }

    @Test
    void testParseSingleWord() {
        // Arrange
        String text = "Hello";
        List<String> expected = List.of("hello");

        // Act
        List<String> actual = parser.parse(text);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testParseHebrewWithDiacritics() {
        // Arrange
        String text = "אַמְרָפֶ֣ל מֶֽלֶךְ שִׁנְעָ֔ר";
        List<String> expected = List.of(
                "אַמְרָפֶ֣ל",
                "מֶֽלֶךְ",
                "שִׁנְעָ֔ר");

        // Act
        List<String> actual = parser.parse(text);

        // Assert
        assertEquals(expected, actual);
    }


    @Test
    void testParseWithPunctuation() {
        // Arrange
        String text = "Lecę. Wolę tam być dzisiaj! Wiesz - im szybciej, tym lepiej.";
        List<String> expected = List.of(
                "Lecę",
                ".",
                "Wolę",
                "tam",
                "być",
                "dzisiaj",
                "!",
                "Wiesz",
                "-",
                "im",
                "szybciej",
                ",",
                "tym",
                "lepiej",
                "."

        );
        // Act
        List<String> actual = parser.parseWithPunctuation(text);

        // Assert
        assertEquals(expected, actual);
    }
}
