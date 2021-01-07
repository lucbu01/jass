package ch.bbzw.jass.model.dto;

import java.util.UUID;

import ch.bbzw.jass.model.JassGame;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PublicGameDto {

	private UUID id;
	private String creator;
	private Integer players;

	public PublicGameDto(JassGame game) {
		id = game.getId();
		if (!game.getTeams().get(0).getUsers().isEmpty()) {
			creator = game.getTeams().get(0).getUsers().get(0).getName();
		}
		players = game.getAllPlayers().size();
	}

}
