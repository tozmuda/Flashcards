package pl.edu.agh.to2.app.dto;

import java.util.List;

public record SimpleSentenceDTO(List<TextElementDTO> words, int level) {

    public void addTextElementDTO(TextElementDTO textElementDTO) {
        words.add(textElementDTO);
    }
}
