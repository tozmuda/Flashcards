package pl.edu.agh.to2.app.deck;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.to2.app.deck.exceptions.InvalidLanguageCode;
import pl.edu.agh.to2.app.dto.*;
import pl.edu.agh.to2.app.exception.ApiRequestException;
import pl.edu.agh.to2.app.deck.exceptions.FlashcardEmptyException;
import pl.edu.agh.to2.app.deck.exceptions.FlashcardOriginalAlreadyExistsException;
import pl.edu.agh.to2.app.deck.flashcard.Flashcard;

import java.util.List;

@RestController
@RequestMapping("/deck")
class DeckController {

    private final DeckService deckService;
    private final DeckSentencesService deckSentencesService;

    public DeckController(DeckService deckService, DeckSentencesService deckSentencesService) {
        this.deckService = deckService;
        this.deckSentencesService = deckSentencesService;
    }

    /**
     * Endpoint for building the sentence tree in Deck(var originalSentences) except
     * for parts of sentence and levels and returning
     * the tree
     */
    @GetMapping("/sentences")
    public ResponseEntity<List<SentenceDTOGetSentences>> getSentencesToFillWithPartOfSentence() {
        return ResponseEntity.ok(deckSentencesService.buildSentencesTreeAndGetSentences());
    }

    @PostMapping("/selected_parts_of_sentences")
    public ResponseEntity<MessageDTO> fillPartsOfSentences(
            @RequestBody List<List<PartsOfSentenceDTO>> partsOfSentenceDTO) {
        return ResponseEntity.ok(deckSentencesService.fillPartsOfSentences(partsOfSentenceDTO));
    }

    @GetMapping
    public ResponseEntity<List<List<SimpleSentenceDTO>>> getDeck() {
        return ResponseEntity.ok(deckSentencesService.getAllWords());
    }

    @PostMapping("/new_flashcard")
    public ResponseEntity<Flashcard> addSingleFlashcardToDeck(@RequestBody Flashcard flashcard){
        try {
            deckService.addFlashcard(flashcard);
            return ResponseEntity.status(HttpStatus.CREATED).body(flashcard);
        } catch (FlashcardEmptyException | FlashcardOriginalAlreadyExistsException e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<MessageDTO> resetDeck(){
        return ResponseEntity.ok(deckService.resetDeck());
    }

    /**
     * Endpoint for saving flashcards to the database
     * @return ResponseEntity with message about the number of added and ignored flashcards
     */
    @GetMapping("/save")
    public ResponseEntity<MessageDTO> saveDeck(){
        AddedIgnoredStatus status = deckService.saveDeck();
        int addedFlashcards = status.getAddedFlashcards();
        int ignoredFlashcards = status.getIgnoredFlashcards();
        int sum = status.getSum();
        return ResponseEntity.ok(new MessageDTO("Added " + addedFlashcards +
                " flashcards out of " + sum + " flashcards. Ignored " + ignoredFlashcards + " flashcards due to duplicates."));
    }

    /**
     * Endpoint for getting all available languages
     * @return ResponseEntity with status 200 and List of LanguageShortDTO (containing languageName and language Short) if there is at least one available, 400 otherwise
     */
    @GetMapping("/lang")
    public ResponseEntity<List<LanguageNameShortDTO>> getLang(){
        List<LanguageNameShortDTO> result = deckService.getLanguages();
        if(result.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/parts_of_speech")
    public ResponseEntity<List<String>> getPartsOfSpeech(){
        List<String> result = deckService.getPartsOfSpeech();
        if(result.isEmpty()){
            throw new ApiRequestException("Wrong text input or language");
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/set_language")
    public ResponseEntity<MessageDTO> setLanguage(@RequestBody LanguageShortDTO languageShort) throws ApiRequestException {
        try {
            deckService.setLanguage(languageShort.languageShort());
        }
        catch (InvalidLanguageCode e) {
            throw new ApiRequestException("Language code not found");
        }
        return ResponseEntity.ok(new MessageDTO("Language set successfully"));
    }
}
