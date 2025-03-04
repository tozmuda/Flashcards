package pl.edu.agh.to2.app.filegenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CSVController.class)
class CSVControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeckCSVCreatorService deckCSVCreatorService;

    @BeforeEach
    void setUp() {
        Mockito.reset(deckCSVCreatorService);
    }

    @Test
    void testMakeCSVOk() throws Exception {
        // Arrange
        byte[] csvData = "dogs,noun,dog,pies,dog,dog\r\ncats,noun,cat,kot,cat,cat\r\n".getBytes();
        when(deckCSVCreatorService.createCSV()).thenReturn(csvData);

        // Act & Assert
        MvcResult result = mockMvc.perform(get("/csv"))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        assertEquals("attachment; filename=flashcards.csv", result.getResponse().getHeader(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals("text/csv; charset=UTF-8", result.getResponse().getHeader(HttpHeaders.CONTENT_TYPE));
        assertEquals(new String(csvData), result.getResponse().getContentAsString());
    }

    @Test
    void testmakeCSVBad() throws Exception {
        // Arrange
        when(deckCSVCreatorService.createCSV()).thenThrow(new IOException());

        // Act & Assert
        mockMvc.perform(get("/csv"))
                .andExpect(status().isBadRequest());
    }
}