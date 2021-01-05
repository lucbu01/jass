package ch.bbzw.jass.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.bbzw.jass.model.JassMove;

@Repository
public interface JassMoveRepository extends JpaRepository<JassMove, UUID> {

}
