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

@WebMvcTest(PDFController.class)
class PDFControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SentencesPDFCreatorService sentencesPDFCreatorService;

    @BeforeEach
    void setUp() {
        Mockito.reset(sentencesPDFCreatorService);
    }

    @Test
    void testMakePDFShouldReturnPDF() throws Exception {
        byte[] mockPDF = "mock pdf content".getBytes();
        when(sentencesPDFCreatorService.createPDF()).thenReturn(mockPDF);

        MvcResult result = mockMvc.perform(get("/pdf"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("attachment; filename=sentences.pdf", result.getResponse().getHeader(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals(new String(mockPDF), result.getResponse().getContentAsString());

    }

    @Test
    void testMakePDFBad() throws Exception {
        // Arrange
        when(sentencesPDFCreatorService.createPDF()).thenThrow(new IOException());

        // Act & Assert
        mockMvc.perform(get("/pdf"))
                .andExpect(status().isBadRequest());
    }
}



