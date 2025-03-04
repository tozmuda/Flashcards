package pl.edu.agh.to2.app.deck.sentence;

import pl.edu.agh.to2.app.dto.TextElementDTOGetSentences;
import java.util.List;

public interface TextElement {

    TextElementDTOGetSentences toDto(List<String> possiblePartsOfSentence);
    String getPartOfSpeech();
}
