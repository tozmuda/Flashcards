package pl.edu.agh.to2.app.filegenerator;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.edu.agh.to2.app.deck.Deck;
import pl.edu.agh.to2.app.deck.sentence.Punctuation;
import pl.edu.agh.to2.app.deck.sentence.Sentence;
import pl.edu.agh.to2.app.deck.sentence.SimpleSentence;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import pl.edu.agh.to2.app.deck.sentence.TextElement;
import pl.edu.agh.to2.app.deck.sentence.Word;
import pl.edu.agh.to2.app.deck.flashcard.Flashcard;

@Service
public class SentencesPDFCreatorService {
    private final Deck deck;
    private final int fontSize;
    private final int margin;
    private int indentation;
    private final String fileName;


    public SentencesPDFCreatorService(Deck deck,
                                      @Qualifier("fontSize") int fontSize,
                                      @Qualifier("margin") int margin,
                                      @Qualifier("indentation") int indentation,
                                      @Qualifier("fontFilename") String fileName) {
        this.deck = deck;
        this.fontSize = fontSize;
        this.margin = margin;
        this.indentation = indentation;
        this.fileName = fileName;
    }
    int calculateIndentation(int level) {
        return indentation * level;
    }

    double calculateWordWidth(List<String> fields, PDFont font) {
        return fields
                .stream()
                .mapToDouble(field -> {
                    try {
                        return font.getStringWidth(field + " ") / 1000 * fontSize;
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                })
                .max()
                .orElse(0);

    }

    void writeString(String text, double x, double y, PDPageContentStream contentStream, PDFont font) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset((float) x, (float) y);
        contentStream.showText(text + " ");
        contentStream.endText();
    }

    private void writePunctuation(Punctuation punctuation, PDFont font, PDDocument document, PdfCreatorParameters parameters) throws IOException {
        try (PDPageContentStream contentStream = new PDPageContentStream(document,
                parameters.getCurrentPage(),
                PDPageContentStream.AppendMode.APPEND,
                true,
                true)) {
            parameters.decrementStartX(font.getStringWidth(" ") / 1000 * fontSize);
            writeString(punctuation.getSymbol() + " ", parameters.getStartX(), parameters.getStartY(), contentStream, font);
            parameters.incrementStartX(fontSize);
        }
    }

    private void writeColumns(Word word, PDFont font, PDDocument document, PdfCreatorParameters parameters) throws IOException {
        Flashcard flashcard = word.getFlashcard();
        List<String> fields = new ArrayList<>(flashcard.getFields());
        fields.add(word.getPartOfSentence());
        double wordWidth = calculateWordWidth(fields, font);

        if (isNotFittingInLineAtAll(wordWidth, parameters)) {
            Collections.fill(fields, "too long... ");
            wordWidth = font.getStringWidth("too long... ") / 1000 * fontSize;
        }
        else if (isNotFittingInCurrentLine(wordWidth, parameters)) {
            parameters.setStartX(parameters.getBaseStartX())
                    .incrementStartX((double) indentation * parameters.getLevel())
                    .decrementStartY((double) fontSize * fields.size());
        }

        if (isNotFittingCurrentPage(fields.size(), parameters)) {
            PDPage page = new PDPage();
            parameters.setCurrentPage(page)
                    .setStartY(parameters.getBaseStartY());
            document.addPage(parameters.getCurrentPage());

            document.addPage(page);
        }

        double groupStartX = parameters.getStartX();

        try (PDPageContentStream contentStream = new PDPageContentStream(document,
            parameters.getCurrentPage(),
            PDPageContentStream.AppendMode.APPEND,
            true,
            true)) {
            for (String field : fields) {
                writeString(field, groupStartX, parameters.getStartY(), contentStream, font);
                parameters.decrementStartY(fontSize);
            }
        }
        parameters.incrementStartX(wordWidth)
                .setNextSentenceStartY(parameters.getStartY())
                .incrementStartY((double) fontSize * fields.size());
    }

    boolean isNotFittingInLineAtAll(double wordWidth, PdfCreatorParameters parameters) {
        return parameters.getBaseStartX() + indentation * parameters.getLevel() + wordWidth > parameters.getWidth();
    }

    boolean isNotFittingInCurrentLine(double wordWidth, PdfCreatorParameters parameters) {
        return parameters.getStartX() + wordWidth > parameters.getWidth();
    }

    boolean isNotFittingCurrentPage(int fieldsSize, PdfCreatorParameters parameters) {
        return parameters.getStartY() - fontSize * fieldsSize < parameters.getEndPage();
    }

    private void drawLine(PDDocument document, PdfCreatorParameters parameters) throws IOException {
        try (PDPageContentStream contentStream = new PDPageContentStream(document,
                parameters.getCurrentPage(),
                PDPageContentStream.AppendMode.APPEND,
                true,
                true)) {
            contentStream.moveTo((float) parameters.getBaseStartX(), (float) parameters.getStartY());
            contentStream.lineTo((float) parameters.getWidth(), (float) parameters.getStartY());
            contentStream.stroke();
        }
    }

    public byte[] createPDF() throws IOException {

        try (PDDocument document = new PDDocument()) {
            PdfCreatorParameters parameters = new PdfCreatorParameters();
            List<Sentence> sentences = deck.getOriginalSentences();
            PDFont font = PDType0Font.load(document, new File(fileName));
            PDPage page = new PDPage();
            document.addPage(page);
            PDRectangle mediaBox = page.getMediaBox();
            parameters.setCurrentPage(page)
                    .setWidth(mediaBox.getWidth() - 2 * margin)
                    .setEndPage(mediaBox.getLowerLeftY() + margin)
                    .setBaseStartX(mediaBox.getLowerLeftY() + margin)
                    .setBaseStartY(mediaBox.getUpperRightY() - margin)
                    .setStartX(parameters.getBaseStartX())
                    .setStartY(parameters.getBaseStartY())
                    .setNextSentenceStartY(0);


            for (Sentence sentence : sentences) {
                for (SimpleSentence simpleSentence : sentence.getSimpleSentences()) {
                    parameters.setLevel(simpleSentence.getLevel())
                            .incrementStartX(calculateIndentation(parameters.getLevel()));
                    for (TextElement textElement : simpleSentence.getTextElements()) {
                        if (textElement instanceof Word word) {
                            writeColumns(word, font, document, parameters);
                        }
                        else if (textElement instanceof Punctuation punctuation) {
                            writePunctuation(punctuation, font, document, parameters);
                        }
                    }
                    parameters.setStartX(parameters.getBaseStartX())
                            .setStartY(parameters.getNextSentenceStartY())
                            .decrementStartY(fontSize);
                }

                drawLine(document, parameters);
                parameters.decrementStartY(2.0 * fontSize);
            }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.save(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();

        }
    }

    void setIndentation(int i) {
        this.indentation = i;
    }
}
