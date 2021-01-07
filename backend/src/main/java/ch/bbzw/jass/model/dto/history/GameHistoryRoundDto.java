package ch.bbzw.jass.model.dto.history;

import java.util.List;
import java.util.stream.Collectors;

import ch.bbzw.jass.model.JassRound;
import ch.bbzw.jass.model.dto.PlayerDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameHistoryRoundDto {

	private PlayerDto winner;
	private List<GameHistoryMoveDto> moves;

	public GameHistoryRoundDto(JassRound round) {
		winner = new PlayerDto(round.calculateWinner());
		moves = round.getMoves().stream().map(GameHistoryMoveDto::new).collect(Collectors.toList());
	}

}
