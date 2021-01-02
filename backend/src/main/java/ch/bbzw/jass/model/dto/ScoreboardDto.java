package ch.bbzw.jass.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ScoreboardDto {
	int teamOnePoints;
	int teamTwoPoints;
	Integer winnerTeamIndex;

	public ScoreboardDto(int teamOnePoints, int teamTwoPoints) {
		this.teamOnePoints = teamOnePoints;
		this.teamTwoPoints = teamTwoPoints;
		if (this.teamOnePoints >= 3000) {
			winnerTeamIndex = 0;
		} else if (this.teamTwoPoints >= 3000) {
			winnerTeamIndex = 1;
		}
	}
}
