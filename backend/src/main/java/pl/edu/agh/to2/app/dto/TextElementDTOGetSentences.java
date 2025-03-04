package pl.edu.agh.to2.app.dto;

import java.util.List;

public record TextElementDTOGetSentences(String originalText, List<String> possiblePartsOfSentence) {

}
