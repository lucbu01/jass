package ch.bbzw.jass.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import ch.bbzw.jass.model.JassGame;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameDto {
	private List<TeamDto> teams;
	
	public GameDto(JassGame game) {
		this.teams = game.getTeams().stream().map(TeamDto::new).collect(Collectors.toList());
	}
}
