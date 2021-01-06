package ch.bbzw.jass.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Entity;
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
@IdClass(JassRoundId.class)
public class JassRound {

	@ManyToOne
	@Id
	private JassMatch match;

	@Id
	private Short index;

	@OneToMany
	@OrderBy("index")
	private List<JassMove> moves = new ArrayList<>();

	public JassRound(JassMatch match, Short index) {
		this.match = match;
		this.index = index;
	}

	public boolean isTheTurn(JassUser user) {
		return user.getId().equals(getTurnOf().getId());
	}

	public JassUser getTurnOf() {
		if (moves.size() > 3) {
			return null;
		}
		JassUser beginner;
		if (index == 0) {
			beginner = match.getAnnouncer();
			JassMatchType type = match.getType();
			if (type == null) {
				return null;
			}
			if (type.isDefinitiveAnnouncerCanBegin()) {
				beginner = match.getDefinitiveAnnouncer();
			}
		} else {
			beginner = match.getRounds().get(index - 1).calculateWinner();
		}
		List<JassUser> allPlayers = match.getGame().getAllPlayersSorted();
		int indexOfBeginner = allPlayers.indexOf(beginner);
		int indexOfActivePlayer = indexOfBeginner + moves.size();
		if (indexOfActivePlayer > 3) {
			indexOfActivePlayer -= 4;
		}
		return allPlayers.get(indexOfActivePlayer);
	}

	public JassUser calculateWinner() {
		JassMove winnerMove = match.getType().calculateWinner(moves, getColor(), index);
		return winnerMove.getUser();
	}

	public boolean canPlay(JassCard card) {
		List<JassCard> hand = match.getHand(getTurnOf());
		if (hand.contains(card)) {
			JassColor color = getColor();
			long count = hand.stream().filter(c -> c.getColor() == color).count();
			boolean isTrumpUnder = false;
			if (count == 1 && color == match.getType().getTrumpColor()) {
				Optional<JassCard> colorCard = hand.stream().filter(c -> c.getColor() == color).findFirst();
				isTrumpUnder = colorCard.isPresent() && colorCard.get().getValue() == JassValue.UNDER;
			}
			return color == null || color == card.getColor() || match.getType().getTrumpColor() == card.getColor()
					|| count == 0 || isTrumpUnder;
		}
		return false;
	}

	public JassColor getColor() {
		if (!this.moves.isEmpty()) {
			return this.moves.get(0).getColor();
		} else {
			return null;
		}
	}

	public boolean isFinished() {
		return this.moves.size() == 4;
	}
}
