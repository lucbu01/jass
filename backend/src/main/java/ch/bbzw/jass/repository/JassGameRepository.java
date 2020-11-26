package ch.bbzw.jass.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.bbzw.jass.model.JassGame;

@Repository
public interface JassGameRepository extends JpaRepository<JassGame, UUID> {

}
