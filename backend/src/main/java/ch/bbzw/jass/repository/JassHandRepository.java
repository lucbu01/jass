package ch.bbzw.jass.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.bbzw.jass.model.JassHand;
import ch.bbzw.jass.model.JassHandId;

public interface JassHandRepository extends JpaRepository<JassHand, JassHandId> {

}
