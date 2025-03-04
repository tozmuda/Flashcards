package pl.edu.agh.to2.app.deck.database;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dictionary")
public class FlashcardDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String baseForm;
    private String translation;

    public FlashcardDTO() {}

    public FlashcardDTO(String baseForm, String translation) {
        this.baseForm = baseForm;
        this.translation = translation;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getBaseForm() {
        return baseForm;
    }

    public void setBaseForm(String original) {
        this.baseForm = original;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
