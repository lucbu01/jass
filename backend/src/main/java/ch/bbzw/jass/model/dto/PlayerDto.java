package ch.bbzw.jass.model.dto;

import java.util.UUID;

import ch.bbzw.jass.model.JassUser;
import lombok.Data;

@Data
public class PlayerDto {
	private UUID id;
	private String name;

	public PlayerDto(JassUser user) {
		this.id = user.getId();
		this.name = user.getName();
	}
}
