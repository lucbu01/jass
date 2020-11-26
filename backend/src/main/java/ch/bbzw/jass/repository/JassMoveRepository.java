package ch.bbzw.jass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.bbzw.jass.model.JassMove;
import ch.bbzw.jass.model.JassMoveId;

@Repository
public interface JassMoveRepository extends JpaRepository<JassMove, JassMoveId> {

}
