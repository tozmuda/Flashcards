package pl.edu.agh.to2.app.deck;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pl.edu.agh.to2.app.deck.database.FlashcardDTO;
import pl.edu.agh.to2.app.deck.database.FlashcardRepository;
import pl.edu.agh.to2.app.deck.exceptions.InvalidLanguageCode;
import pl.edu.agh.to2.app.deck.language.LanguageLoader;
import pl.edu.agh.to2.app.dto.LanguageNameShortDTO;
import pl.edu.agh.to2.app.dto.MessageDTO;
import pl.edu.agh.to2.app.deck.flashcard.Flashcard;

import java.text.MessageFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
class DeckService {

    private final Deck deck;
    private final LanguageLoader languageLoader;
    private final FlashcardRepository flashcardRepository;
    private final Logger logger = Logger.getLogger(DeckService.class.getName());

    public DeckService(Deck deck, FlashcardRepository flashcardRepository, LanguageLoader languageLoader) {
        this.deck = deck;
        this.flashcardRepository = flashcardRepository;
        this.languageLoader = languageLoader;
    }

    public void addFlashcard(Flashcard flashcard) {
        deck.addFlashcard(flashcard);
    }

    public MessageDTO resetDeck(){
        deck.reset();
        return new MessageDTO("Deck has been reset");
    }

    public AddedIgnoredStatus saveDeck() {
        AddedIgnoredStatus status = new AddedIgnoredStatus();
        for (Flashcard flashcard : deck.getAllFlashcards()) {
            try {
                flashcardRepository.save(new FlashcardDTO(flashcard.baseForm(), flashcard.translation()));
                status.incrementAddedFlashcards();
            } catch (DataIntegrityViolationException e) {
                status.incrementIgnoredFlashcards();
                logger.log(Level.INFO,
                        MessageFormat.format("Ignoring duplicate flashcard number {0}: {1}", status.getIgnoredFlashcards(), flashcard.original()));
            }
        }
        return status;
    }

    public List<LanguageNameShortDTO> getLanguages(){
        return this.languageLoader.getConfig().getLanguages().stream()
                .map(language -> new LanguageNameShortDTO(language.getName(), language.getShortCode()))
                .toList();
    }

    public List<String> getPartsOfSpeech(){
        if (deck.getLanguageShort() == null){
            return new ArrayList<>();
        }
        String languageShort = this.deck.getLanguageShort();
        List<String> allPartsOfSpeech = new ArrayList<>();
        this.languageLoader.getConfig().getLanguages().stream()
                .filter(language -> language.getShortCode().equals(languageShort))
                .findFirst()
                .ifPresent(language -> language.getParts().forEach(part -> allPartsOfSpeech.add(part.getPartOfSpeech())));

        return allPartsOfSpeech;
    }


    public void setLanguage(String languageShort) throws InvalidLanguageCode {
        deck.setLanguage(languageShort);
    }
}
