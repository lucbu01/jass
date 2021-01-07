package ch.bbzw.jass.model.dto.history;

import ch.bbzw.jass.model.JassCard;
import ch.bbzw.jass.model.JassMove;
import ch.bbzw.jass.model.dto.PlayerDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameHistoryMoveDto {

	PlayerDto player;
	JassCard card;

	public GameHistoryMoveDto(JassMove move) {
		player = new PlayerDto(move.getUser());
		card = move.getCard();
	}

}
