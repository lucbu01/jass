package ch.bbzw.jass.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class JassGame {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@OneToMany(mappedBy = "game")
	private List<JassTeam> team = new ArrayList<>();

	@OneToMany(mappedBy = "game")
	private List<JassMatch> matches = new ArrayList<>();
}
