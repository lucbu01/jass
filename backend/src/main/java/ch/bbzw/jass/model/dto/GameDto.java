package ch.bbzw.jass.model.dto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import ch.bbzw.jass.model.JassGame;
import ch.bbzw.jass.model.JassMatch;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class GameDto {
	private UUID id;
	private List<TeamDto> teams;
	private Boolean started;
	private MatchDto match;

	public GameDto(JassGame game) {
		this(game, false);
	}

	public GameDto(JassGame game, boolean withMatch) {
		this.id = game.getId();
		this.teams = game.getTeams().stream().map(TeamDto::new).collect(Collectors.toList());
		this.started = game.getStarted();
		if (game.getMatches().size() > 0) {
			JassMatch match = game.getMatches().get(game.getMatches().size() - 1);
			this.match = new MatchDto(match.getIndex(), new PlayerDto(match.getAnnouncer()),
					new PlayerDto(match.getDefinitiveAnnouncer()), match.isPushed());
		}
	}

	public GameDto(JassMatch match) {
		this.match = new MatchDto(match.getIndex(), new PlayerDto(match.getAnnouncer()),
				new PlayerDto(match.getDefinitiveAnnouncer()), match.isPushed());
	}
}
