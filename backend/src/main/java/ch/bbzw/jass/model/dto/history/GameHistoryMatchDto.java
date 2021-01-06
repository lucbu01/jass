package ch.bbzw.jass.model.dto.history;

import java.util.List;
import java.util.stream.Collectors;

import ch.bbzw.jass.model.JassMatch;
import ch.bbzw.jass.model.JassMatchType;
import ch.bbzw.jass.model.dto.PlayerDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameHistoryMatchDto {

	private int teamOnePoints;
	private int teamTwoPoints;
	private JassMatchType type;
	private PlayerDto announcer;
	private PlayerDto definitiveAnnouncer;
	private List<GameHistoryRoundDto> rounds;

	public GameHistoryMatchDto(JassMatch match) {
		teamOnePoints = match.calculatePointsOfTeam(match.getGame().getTeams().get(0));
		teamTwoPoints = match.calculatePointsOfTeam(match.getGame().getTeams().get(1));
		type = match.getType();
		announcer = new PlayerDto(match.getAnnouncer());
		definitiveAnnouncer = new PlayerDto(match.getDefinitiveAnnouncer());
		rounds = match.getRounds().stream().map(GameHistoryRoundDto::new).collect(Collectors.toList());
	}

}
