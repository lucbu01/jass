package ch.bbzw.jass.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import ch.bbzw.jass.model.JassGame;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ScoreboardDto {
	int teamOnePoints;
	int teamTwoPoints;
	boolean finished;
	Byte winnerTeamIndex;

	public ScoreboardDto(JassGame game) {
		this.teamOnePoints = game.calculatePointsOfTeamOne();
		this.teamTwoPoints = game.calculatePointsOfTeamTwo();
		boolean f = game.isFinished();
		this.finished = f;
		if (game.isFinished()) {
			winnerTeamIndex = game.getWinnerTeam().getIndex();
		}
	}
}
