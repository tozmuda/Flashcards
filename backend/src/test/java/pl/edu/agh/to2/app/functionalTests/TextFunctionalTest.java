package pl.edu.agh.to2.app.functionalTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.edu.agh.to2.app.dto.TextDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TextFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void parseTextWithWordsTest() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest = mapper.writeValueAsString(new TextDTO("Hello, world! Hello again!"));

        List<String> expected = List.of("hello", "world", "again");
        String expectedJson = mapper.writeValueAsString(expected);

        // Act & Assert
        MvcResult result = mockMvc.perform(post("/text")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(expectedJson, result.getResponse().getContentAsString());
    }

    @Test
    void parseText_NoWordsTest() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest = mapper.writeValueAsString(new TextDTO(":!;\"''*1234 56 7"));

        // Act & Assert
        mockMvc.perform(post("/text")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }
}
