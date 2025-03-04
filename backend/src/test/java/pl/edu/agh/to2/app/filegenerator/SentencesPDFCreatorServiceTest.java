package pl.edu.agh.to2.app.filegenerator;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.edu.agh.to2.app.config.AppConfig;
import pl.edu.agh.to2.app.deck.Deck;
import pl.edu.agh.to2.app.deck.flashcard.Flashcard;
import pl.edu.agh.to2.app.deck.sentence.Punctuation;
import pl.edu.agh.to2.app.deck.sentence.Sentence;
import pl.edu.agh.to2.app.deck.sentence.SimpleSentence;
import pl.edu.agh.to2.app.deck.sentence.Word;


import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
class SentencesPDFCreatorServiceTest {

    @Mock
    private Deck deck;

    @Autowired
    @Qualifier("fontSize")
    private int fontSize;

    @Autowired
    @Qualifier("margin")
    private int margin;

    @Autowired
    @Qualifier("indentation")
    private int indentation;

    @Autowired
    @Qualifier("fontFilename")
    private String fontFilename;

    SentencesPDFCreatorService service;
    @BeforeEach
    void setUp() {
        service = new SentencesPDFCreatorService(deck, fontSize, margin, indentation, fontFilename);
    }

    @Test
    void testCalculateWordWidth() throws IOException {
        // Arrange
        List<String> exampleList= List.of(
                "Elephant",
                "Słoń",
                "elephant",
                "elifant",
                "elefant",
                "noun",
                "subject"
        );
        PDFont font = mock(PDType0Font.class);
        when(font.getStringWidth(Mockito.anyString())).thenReturn(500f);

        double result = service.calculateWordWidth(exampleList, font);

        assertEquals(6.0f, result);
    }

    @Test
    void calculateIndentationLevelZero() {
        int result = service.calculateIndentation(0);
        assertEquals(0, result);
    }

    @Test
    void calculateIndentationLevelOne() {
        int result = service.calculateIndentation(1);
        assertEquals(indentation, result);
    }


    @Test
    void writeStringValidInput() throws IOException {
        PDFont font = mock(PDType0Font.class);
        PDPageContentStream contentStream = mock(PDPageContentStream.class);

        service.writeString("Hello", 100, 200, contentStream, font);

        verify(contentStream).beginText();
        verify(contentStream).setFont(font, fontSize);
        verify(contentStream).newLineAtOffset(100, 200);
        verify(contentStream).showText("Hello ");
        verify(contentStream).endText();
    }

    @Test
    void createPDFReturnsNonEmptyByteArray() throws IOException {
        Word word = new Word(new Flashcard("pies", "dog", "pies", "noun", "pies", "pies"));
        word.setPartOfSentence("subject");
        Punctuation punctuation = new Punctuation(".");
        SimpleSentence simpleSentence = new SimpleSentence();
        simpleSentence.addTextElement(word);
        simpleSentence.addTextElement(punctuation);
        Sentence sentence = new Sentence();
        sentence.addSimpleSentence(simpleSentence);
        when(deck.getOriginalSentences()).thenReturn(List.of(sentence));

        byte[] result = service.createPDF();

        assertTrue(result.length > 0);
    }


    @Test
    void isNotFittingInLineAtAllReturnsTrueWhenWordFitsInLine() {
        PdfCreatorParameters parameters = new PdfCreatorParameters()
                .setBaseStartX(10)
                .setLevel(2)
                .setWidth(100);
        service.setIndentation(5);
        double wordWidth = 50;

        boolean result = service.isNotFittingInLineAtAll(wordWidth, parameters);

        assertFalse(result);
    }

    @Test
    void isNotFittingInLineAtAllReturnsFalseWhenWordDoesNotFitInLine() {
        PdfCreatorParameters parameters = new PdfCreatorParameters()
                .setBaseStartX(10)
                .setLevel(2)
                .setWidth(50);
        service.setIndentation(5);
        double wordWidth = 50;

        boolean result = service.isNotFittingInLineAtAll(wordWidth, parameters);

        assertTrue(result);
    }

    @Test
    void isNotFittingInCurrentLineReturnsTrueWhenWordFitsInLine() {
        PdfCreatorParameters parameters = new PdfCreatorParameters()
                .setStartX(50)
                .setWidth(100);
        double wordWidth = 40;

        boolean result = service.isNotFittingInCurrentLine(wordWidth, parameters);

        assertFalse(result);
    }

    @Test
    void isNotFittingInCurrentLineReturnsFalseWhenWordDoesNotFitInLine() {
        PdfCreatorParameters parameters = new PdfCreatorParameters()
                .setStartX(50)
                .setWidth(80);
        double wordWidth = 40;

        boolean result = service.isNotFittingInCurrentLine(wordWidth, parameters);

        assertTrue(result);
    }

    @Test
    void isNotFittingCurrentPageReturnsTrueWhenFieldsFitInPage() {
        PdfCreatorParameters parameters = new PdfCreatorParameters()
                .setStartY(100)
                .setEndPage(50);
        int fieldsSize = 4;

        boolean result = service.isNotFittingCurrentPage(fieldsSize, parameters);

        assertFalse(result);
    }

    @Test
    void isNotFittingCurrentPageReturnsFalseWhenFieldsDoNotFitInPage() {
        PdfCreatorParameters parameters = new PdfCreatorParameters()
                .setStartY(100)
                .setEndPage(90);
        int fieldsSize = 4;

        boolean result = service.isNotFittingCurrentPage(fieldsSize, parameters);

        assertTrue(result);
    }
}