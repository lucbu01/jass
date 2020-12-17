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

	private Boolean started;

	@OneToMany(mappedBy = "game")
	private List<JassTeam> teams = new ArrayList<>();

	@OneToMany(mappedBy = "game")
	private List<JassMatch> matches = new ArrayList<>();

	public List<JassUser> getAllPlayers() {
		List<JassUser> players = new ArrayList<>();
		teams.stream().forEach(team -> players.addAll(team.getUsers()));
		return players;
	}

	public List<JassCard> getActualHand(JassUser player) {
		if (getMatches().size() > 0) {
			return getMatches().get(getMatches().size() - 1).getHand(player);
		}
		return new ArrayList<>();
	}
}
