package ch.bbzw.jass.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import ch.bbzw.jass.model.JassCard;
import ch.bbzw.jass.model.JassMove;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class MoveDto {

	private Byte index;
	private PlayerDto player;
	private JassCard card;

	public MoveDto(JassMove move) {
		this.index = move.getIndex();
		this.player = new PlayerDto(move.getUser());
		this.card = move.getCard();
	}
}
