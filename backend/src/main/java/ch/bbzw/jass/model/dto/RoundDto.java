package ch.bbzw.jass.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import ch.bbzw.jass.model.JassRound;
import ch.bbzw.jass.model.JassUser;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class RoundDto {
	private Short index;
	private PlayerDto turnOf;
	private Boolean finished;
	private List<MoveDto> moves;

	public RoundDto(JassRound round) {
		this.index = round.getIndex();
		JassUser player = round.getTurnOf();
		if (player != null) {
			this.turnOf = new PlayerDto(player);
		}
		this.finished = round.isFinished();
		this.moves = round.getMoves().stream().map(MoveDto::new).collect(Collectors.toList());
	}
}
