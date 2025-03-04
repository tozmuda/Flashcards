package pl.edu.agh.to2.app.dto;

public record TextElementDTO(String original, String baseForm, String partOfSpeech, String partOfSentence,
                             String translation, String transliteration, String transcription) {
}
