package ch.bbzw.jass.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import ch.bbzw.jass.model.JassTeam;
import lombok.Data;

@Data
public class TeamDto {
	Byte index;
	List<PlayerDto> players;
	
	public TeamDto(JassTeam team) {
		this.index = team.getIndex();
		this.players = team.getUsers().stream().map(PlayerDto::new).collect(Collectors.toList());
	}
}
