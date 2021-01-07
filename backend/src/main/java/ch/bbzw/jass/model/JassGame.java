package ch.bbzw.jass.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class JassGame {
	public static final int WIN_POINTS = 3000;

	public JassGame(boolean isPublic) {
		this.publicGame = isPublic;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	private boolean started = false;

	private boolean publicGame = false;

	@OneToMany(mappedBy = "game")
	@OrderBy("index")
	private List<JassTeam> teams = new ArrayList<>();

	@OneToMany(mappedBy = "game")
	@OrderBy("index")
	private List<JassMatch> matches = new ArrayList<>();

	public List<JassUser> getAllPlayers() {
		List<JassUser> players = new ArrayList<>();
		teams.stream().forEach(team -> players.addAll(team.getUsers()));
		return players;
	}

	public List<JassUser> getAllPlayersSorted() {
		List<JassUser> players = new ArrayList<>();
		if (teams.size() == 2 && teams.get(0).getUsers().size() == 2 && teams.get(1).getUsers().size() == 2) {
			players.add(teams.get(0).getUsers().get(0));
			players.add(teams.get(1).getUsers().get(0));
			players.add(teams.get(0).getUsers().get(1));
			players.add(teams.get(1).getUsers().get(1));
		}
		return players;
	}

	public List<JassCard> getActualHand(JassUser player) {
		if (getMatches().size() > 0) {
			return getMatches().get(getMatches().size() - 1).getHand(player);
		}
		return new ArrayList<>();
	}

	public int calculatePointsOfTeam(JassTeam team) {
		int points = 0;
		for (JassMatch match : getMatches()) {
			points += match.calculatePointsOfTeam(team);
		}
		return points;
	}

	public int calculatePointsOfTeamOne() {
		JassTeam teamOne = getTeams().get(0);
		return calculatePointsOfTeam(teamOne);
	}

	public int calculatePointsOfTeamTwo() {
		JassTeam teamTwo = getTeams().get(1);
		return calculatePointsOfTeam(teamTwo);
	}

	public boolean isFinished() {
		return calculatePointsOfTeamOne() >= WIN_POINTS || calculatePointsOfTeamTwo() >= WIN_POINTS;
	}

	public JassTeam getWinnerTeam() {
		if (calculatePointsOfTeamOne() >= WIN_POINTS) {
			if (calculatePointsOfTeamTwo() < WIN_POINTS) {
				return teams.get(0);
			}
		} else if (calculatePointsOfTeamTwo() >= WIN_POINTS) {
			return teams.get(1);
		}
		return null;
	}

}
