package ch.bbzw.jass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.bbzw.jass.model.JassRound;
import ch.bbzw.jass.model.JassRoundId;

@Repository
public interface JassRoundRepository extends JpaRepository<JassRound, JassRoundId> {

}
