package ch.bbzw.jass.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@IdClass(JassMatchId.class)
public class JassMatch {

	@ManyToOne
	@Id
	private JassGame game;

	@Id
	private Integer index;

	@OneToMany
	@OrderBy("index")
	private List<JassRound> rounds = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	private JassMatchType type;

	@ManyToOne
	private JassUser announcer;

	@OneToMany
	private List<JassHand> handItems = new ArrayList<>();

	private boolean pushed = false;

	public JassMatch(JassGame game, Integer index) {
		this.game = game;
		this.index = index;
	}

	public List<JassHand> getHandItems(JassUser player) {
		return getHandItems().stream().filter(item -> item.getUser().getId().equals(player.getId()))
				.collect(Collectors.toList());
	}

	public List<JassCard> getHand(JassUser player) {
		return JassCards.sort(getHandItems(player).stream().map(item -> item.getCard()).collect(Collectors.toList()));
	}

	public JassUser getDefinitiveAnnouncer() {
		if (isPushed()) {
			JassTeam team = getGame().getTeams().stream().filter(t -> t.getUsers().contains(getAnnouncer())).findFirst()
					.get();
			return team.getUsers().stream().filter(p -> !p.equals(getAnnouncer())).findFirst().get();
		}
		return getAnnouncer();
	}

	public int calculatePointsOfTeam(JassTeam team) {
		return type.calculatePointsOfTeam(team, rounds);
	}
}
