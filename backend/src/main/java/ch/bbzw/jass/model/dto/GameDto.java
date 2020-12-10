package ch.bbzw.jass.model.dto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import ch.bbzw.jass.model.JassGame;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class GameDto {
	private UUID id;
	private List<TeamDto> teams;
	private Boolean started;
	
	public GameDto(JassGame game) {
		this.id = game.getId();
		this.teams = game.getTeams().stream().map(TeamDto::new).collect(Collectors.toList());
		this.started = game.getStarted();
	}
}