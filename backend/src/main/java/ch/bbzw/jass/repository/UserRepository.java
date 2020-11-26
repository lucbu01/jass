package ch.bbzw.jass.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.bbzw.jass.model.JassUser;

@Repository
public interface UserRepository extends JpaRepository<JassUser, UUID> {

}
