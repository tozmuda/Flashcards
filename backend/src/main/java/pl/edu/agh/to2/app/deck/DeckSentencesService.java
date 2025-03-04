package pl.edu.agh.to2.app.deck;

import org.springframework.stereotype.Service;
import pl.edu.agh.to2.app.deck.exceptions.LanguageOrPartOfSpeechNotFound;
import pl.edu.agh.to2.app.deck.flashcard.Flashcard;
import pl.edu.agh.to2.app.deck.sentence.Punctuation;
import pl.edu.agh.to2.app.deck.sentence.Sentence;
import pl.edu.agh.to2.app.deck.sentence.SimpleSentence;
import pl.edu.agh.to2.app.deck.sentence.TextElement;
import pl.edu.agh.to2.app.deck.sentence.Word;
import pl.edu.agh.to2.app.dto.MessageDTO;
import pl.edu.agh.to2.app.dto.PartsOfSentenceDTO;
import pl.edu.agh.to2.app.dto.SentenceDTOGetSentences;
import pl.edu.agh.to2.app.dto.SimpleSentenceDTO;
import pl.edu.agh.to2.app.dto.SimpleSentenceDTOGetSentences;
import pl.edu.agh.to2.app.dto.TextElementDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeckSentencesService {

    private final Deck deck;

    public DeckSentencesService(Deck deck) {
        this.deck = deck;
    }

    public MessageDTO fillPartsOfSentences(List<List<PartsOfSentenceDTO>> partsOfSentenceDTO) {
        // Iterujemy przez wszystkie grupy zdań
        for (int sentenceIndex = 0; sentenceIndex < partsOfSentenceDTO.size(); sentenceIndex++) {
            List<PartsOfSentenceDTO> sentenceParts = partsOfSentenceDTO.get(sentenceIndex);
            List<SimpleSentence> simpleSentences = deck.getOriginalSentences().get(sentenceIndex).getSimpleSentences();

            // Iterujemy przez proste zdania
            for (int simpleSentenceIndex = 0; simpleSentenceIndex < sentenceParts.size(); simpleSentenceIndex++) {
                PartsOfSentenceDTO partsDTO = sentenceParts.get(simpleSentenceIndex);
                SimpleSentence simpleSentence = simpleSentences.get(simpleSentenceIndex);

                // Ustawiamy poziom prostego zdania
                simpleSentence.setLevel(partsDTO.level());

                // Iterujemy przez elementy tekstowe w prostym zdaniu
                List<TextElement> textElements = simpleSentence.getTextElements();
                List<String> selectedParts = partsDTO.selectedPartsOfSentence();

                for (int elementIndex = 0; elementIndex < textElements.size(); elementIndex++) {
                    TextElement textElement = textElements.get(elementIndex);

                    // Sprawdzamy, czy element jest typu Word i ustawiamy część zdania
                    if (textElement instanceof Word word) {
                        word.setPartOfSentence(selectedParts.get(elementIndex));
                    }
                }
            }
        }

        return new MessageDTO("Saved levels and parts of sentences");
    }


    public List<List<SimpleSentenceDTO>> getAllWords() {
        List<List<SimpleSentenceDTO>> sentences = new ArrayList<>();
        for (Sentence sentence : deck.getOriginalSentences()) {
            List<SimpleSentenceDTO> innerSentences = new ArrayList<>();
            for (SimpleSentence simpleSentence : sentence.getSimpleSentences()) {
                SimpleSentenceDTO innerSentence = new SimpleSentenceDTO(new ArrayList<>(), simpleSentence.getLevel());
                for (TextElement textElement : simpleSentence.getTextElements()) {
                    if (textElement instanceof Word word) {
                        innerSentence.addTextElementDTO(new TextElementDTO(
                                word.getFlashcard().original(),
                                word.getFlashcard().baseForm(),
                                word.getFlashcard().partOfSpeech(),
                                word.getPartOfSentence(),
                                word.getFlashcard().translation(),
                                word.getFlashcard().transliteration(),
                                word.getFlashcard().transcription()
                        ));
                    }
                }
                innerSentences.add(innerSentence);
            }
            sentences.add(innerSentences);
        }
        return sentences;
    }

    public List<String> getPossiblePartsOfSentence(String partOfSpeech) {
        if (partOfSpeech == null) { // Gdy rozważamy Punctuation, a nie Word
            return new ArrayList<>();
        }
        return deck.getLanguage()
                .getParts()
                .stream()
                .filter(part -> part.getPartOfSpeech().equals(partOfSpeech))
                .findFirst()
                .orElseThrow(LanguageOrPartOfSpeechNotFound::new)
                .getPossiblePartsOfSentence()
                .stream()
                .toList();
    }

    public List<SentenceDTOGetSentences> buildSentencesTreeAndGetSentences() {
        buildSentencesTree();
        return getSentences();
    }

    private void buildSentencesTree() {
        // Tworzenie drzewa List<Sentence>

        Sentence sentence = new Sentence();
        SimpleSentence simpleSentence = new SimpleSentence();
        for (String textElementAsString : deck.getOriginalText()) {
            if (textElementAsString.equals(".")) {
                simpleSentence.addTextElement(new Punctuation("."));
                sentence.addSimpleSentence(simpleSentence);
                deck.getOriginalSentences().add(sentence);
                simpleSentence = new SimpleSentence();
                sentence = new Sentence();
                continue;
            }
            Flashcard flashcard = deck.getFlashcards().get(textElementAsString.toLowerCase());
            if (flashcard != null && flashcard.partOfSpeech().equals(deck.getLanguage().getConjunction())) {
                sentence.addSimpleSentence(simpleSentence);
                simpleSentence = new SimpleSentence();
            }
            if (flashcard != null) {
                simpleSentence.addTextElement(new Word(flashcard));
            } else {
                simpleSentence.addTextElement(new Punctuation(textElementAsString));
            }
        }
    }

    private List<SentenceDTOGetSentences> getSentences() {
        List<SentenceDTOGetSentences> sentencesDTO2 = new ArrayList<>();
        for (Sentence sentence1 : deck.getOriginalSentences()) {
            List<SimpleSentenceDTOGetSentences> simpleSentencesDTO2 = new ArrayList<>();
            for (SimpleSentence simpleSentence1 : sentence1.getSimpleSentences()) {
                SimpleSentenceDTOGetSentences simpleSentenceDTOGetSentences = new SimpleSentenceDTOGetSentences(new ArrayList<>());
                for (TextElement textElement : simpleSentence1.getTextElements()) {
                    simpleSentenceDTOGetSentences.words().add(textElement.toDto(
                            getPossiblePartsOfSentence(textElement.getPartOfSpeech())
                    ));
                }
                simpleSentencesDTO2.add(new SimpleSentenceDTOGetSentences(new ArrayList<>(simpleSentenceDTOGetSentences.words())));
            }
            sentencesDTO2.add(new SentenceDTOGetSentences(new ArrayList<>(simpleSentencesDTO2)));
        }
        return sentencesDTO2;
    }

    public Deck getDeck() {
        return deck;
    }
}
