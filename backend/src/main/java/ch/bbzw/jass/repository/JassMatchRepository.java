package ch.bbzw.jass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.bbzw.jass.model.JassMatch;
import ch.bbzw.jass.model.JassMatchId;

@Repository
public interface JassMatchRepository extends JpaRepository<JassMatch, JassMatchId> {

}
