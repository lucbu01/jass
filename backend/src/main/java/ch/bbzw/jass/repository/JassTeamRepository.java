package ch.bbzw.jass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.bbzw.jass.model.JassTeam;
import ch.bbzw.jass.model.JassTeamId;

@Repository
public interface JassTeamRepository extends JpaRepository<JassTeam, JassTeamId> {
}
