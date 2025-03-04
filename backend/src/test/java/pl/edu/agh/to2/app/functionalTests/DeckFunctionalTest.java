package pl.edu.agh.to2.app.functionalTests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.edu.agh.to2.app.deck.flashcard.Flashcard;
import pl.edu.agh.to2.app.dto.LanguageShortDTO;
import pl.edu.agh.to2.app.dto.PartsOfSentenceDTO;
import pl.edu.agh.to2.app.dto.SimpleSentenceDTO;
import pl.edu.agh.to2.app.dto.TextDTO;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DeckFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    private final Flashcard flashcard1 = new Flashcard(
            "hello",
            "cześć",
            "hello",
            "not selected",
            "heloł",
            "helloł"
    );

    private final Flashcard flashcardEmpty = new Flashcard(
            "hi",
            "",
            "",
            "not selected",
            "",
            ""
    );


    @Test
    void addFlashcardToDeckTest() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest = mapper.writeValueAsString(flashcard1);

        String expectedJson = jsonRequest;

        // Act & Assert
        MvcResult result = mockMvc.perform(post("/deck/new_flashcard")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        assertEquals(expectedJson, result.getResponse().getContentAsString());
    }

    @Test
    void addEmptyFlashcardToDeckTest() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest = mapper.writeValueAsString(flashcardEmpty);

        // Act & Assert
        mockMvc.perform(post("/deck/new_flashcard")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());

        // Cleanup
        mockMvc.perform(delete("/deck"))
                .andExpect(status().isOk());
    }

    @Test
    void addDuplicateFlashcardToDeckTest() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest1 = mapper.writeValueAsString(flashcard1);
        String jsonRequest2 = mapper.writeValueAsString(flashcard1);


        // Act & Assert
        mockMvc.perform(post("/deck/new_flashcard")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest1));
        mockMvc.perform(post("/deck/new_flashcard")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest2))
            .andExpect(status().isBadRequest());

        // Cleanup
        mockMvc.perform(delete("/deck"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllWordsTest() throws Exception {
        // Arrange
        Flashcard flashcard3 = new Flashcard("Ala", "ala", "ala", "noun", "", "");
        Flashcard flashcard4 = new Flashcard("ma", "has", "miec", "verb", "", "");
        Flashcard flashcard5 = new Flashcard("kota", "cat", "kot", "noun", "", "");
        Flashcard flashcard6 = new Flashcard("a", "and", "a", "conjunction", "", "");

        LanguageShortDTO languageShortDTO = new LanguageShortDTO("PL");

        ObjectMapper objectMapper = new ObjectMapper();
        String languageShortDTOJson = objectMapper.writeValueAsString(languageShortDTO);

        TextDTO textDTO = new TextDTO("Ala ma kota, a kota ma ala.");
        String textDTOJson = objectMapper.writeValueAsString(textDTO);

        List<String> flashcardsJsons = new ArrayList<>();
        flashcardsJsons.add(objectMapper.writeValueAsString(flashcard3));
        flashcardsJsons.add(objectMapper.writeValueAsString(flashcard4));
        flashcardsJsons.add(objectMapper.writeValueAsString(flashcard5));
        flashcardsJsons.add(objectMapper.writeValueAsString(flashcard6));

        List<String> selectedPartsOfSentence1 = List.of("subject", "predicate", "subject");
        List<String> selectedPartsOfSentence2 = List.of("connector", "subject", "predicate", "subject");
        int level1 = 1;
        int level2 = 0;

        PartsOfSentenceDTO partsOfSentenceDTO1 = new PartsOfSentenceDTO(level1, selectedPartsOfSentence1);
        PartsOfSentenceDTO partsOfSentenceDTO2 = new PartsOfSentenceDTO(level2, selectedPartsOfSentence2);

        List<PartsOfSentenceDTO> partsOfSentenceDTOS = List.of(partsOfSentenceDTO1, partsOfSentenceDTO2);
        List<List<PartsOfSentenceDTO>> allPartsOfSentenceDTOs = List.of(partsOfSentenceDTOS);

        String fillPartsOfSentencesJson = objectMapper.writeValueAsString(allPartsOfSentenceDTOs);

        // Act & Assert

        mockMvc.perform(post("/deck/set_language")
                .contentType(MediaType.APPLICATION_JSON)
                .content(languageShortDTOJson))
                .andExpect(status().isOk());

        mockMvc.perform(post("/text")
                .contentType(MediaType.APPLICATION_JSON)
                .content(textDTOJson))
                .andExpect(status().isOk());

        for (String flashcardJson : flashcardsJsons) {
            mockMvc.perform(post("/deck/new_flashcard")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(flashcardJson))
                    .andExpect(status().isCreated());
        }

        mockMvc.perform(get("/deck/sentences"))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(post("/deck/selected_parts_of_sentences")
                .contentType(MediaType.APPLICATION_JSON)
                .content(fillPartsOfSentencesJson))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/deck"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        List<List<SimpleSentenceDTO>> objResult = objectMapper.readValue(jsonResponse,
                new TypeReference<List<List<SimpleSentenceDTO>>>() {});

        assertEquals(1, objResult.size());
        assertEquals(2, objResult.getFirst().size());
        assertEquals(1, objResult.getFirst().getFirst().level());
        assertEquals(0, objResult.getFirst().getLast().level());
        assertEquals("connector", objResult.getFirst().getLast().words().getFirst().partOfSentence());
        assertEquals("kot", objResult.getFirst().getFirst().words().getLast().baseForm());
        assertEquals("and", objResult.getFirst().getLast().words().getFirst().translation());
        assertEquals("verb", objResult.getFirst().getFirst().words().get(1).partOfSpeech());

    }

    @Test
    void resetDeckTest() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest = mapper.writeValueAsString(flashcard1);

        mockMvc.perform(post("/deck/new_flashcard")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest));


        // Act & Assert

        mockMvc.perform(post("/deck/new_flashcard")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());

        mockMvc.perform(delete("/deck"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/deck/new_flashcard")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());

    }
}
