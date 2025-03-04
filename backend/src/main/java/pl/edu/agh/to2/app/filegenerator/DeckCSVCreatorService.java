package pl.edu.agh.to2.app.filegenerator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.edu.agh.to2.app.deck.Deck;
import pl.edu.agh.to2.app.deck.flashcard.Flashcard;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

@Service
class DeckCSVCreatorService {
    Deck deck;
    Charset charset;
    CSVFormat csvFormat;

    public DeckCSVCreatorService(Deck deck, @Qualifier("encoding") Charset charset,
                                 @Qualifier("csvFormat") CSVFormat csvFormat) {
        this.deck = deck;
        this.charset = charset;
        this.csvFormat = csvFormat;
    }

    public byte[] createCSV() throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(byteArrayOutputStream, charset);
             CSVPrinter printer = new CSVPrinter(writer, csvFormat)) {

            for (Flashcard flashcard : deck.getAllFlashcards()) {
                printer.printRecord(flashcard.original(), flashcard.partOfSpeech(), flashcard.baseForm(),
                        flashcard.translation(), flashcard.transcription(), flashcard.transliteration());
            }
            printer.flush();
            return byteArrayOutputStream.toByteArray();
        }

    }

}
