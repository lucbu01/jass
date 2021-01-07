package ch.bbzw.jass.model.dto.history;

import java.util.List;
import java.util.stream.Collectors;

import ch.bbzw.jass.model.JassGame;
import ch.bbzw.jass.model.dto.TeamDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameHistoryDto {

	private int teamOnePoints;
	private int teamTwoPoints;
	private TeamDto winnerTeam;
	private List<TeamDto> teams;
	private List<GameHistoryMatchDto> matches;

	public GameHistoryDto(JassGame game) {
		teamOnePoints = game.calculatePointsOfTeamOne();
		teamTwoPoints = game.calculatePointsOfTeamTwo();
		winnerTeam = new TeamDto(game.getWinnerTeam());
		teams = game.getTeams().stream().map(TeamDto::new).collect(Collectors.toList());
		matches = game.getMatches().stream().map(GameHistoryMatchDto::new).collect(Collectors.toList());
	}

}
