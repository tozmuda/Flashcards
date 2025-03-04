package pl.edu.agh.to2.app.config;

import org.apache.commons.csv.CSVFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Configuration
public class AppConfig {
    @Bean(name = "encoding")
    public Charset getEncoding() {
        return StandardCharsets.UTF_8;
    }
    @Bean (name= "csvFormat")
    public CSVFormat getCSVFormat() {
        return CSVFormat.RFC4180;
    }

    @Bean(name = "fontSize")
    public int getFontSize() {
        return 12;
    }
    @Bean(name = "margin")
    public int getMargin() {
        return 20;
    }
    @Bean(name = "indentation")
    public int getIndentation() {
        return 12;
    }
    @Bean(name = "fontFilename")
    public String getFontFilename() {
        return "src/main/resources/Roboto-Regular.ttf";
    }
}
