package pl.edu.agh.to2.app.deck.database;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FlashcardRepository extends JpaRepository<FlashcardDTO, Long> {

}
