package ch.bbzw.jass.helper;

import java.util.List;
import java.util.Optional;

import ch.bbzw.jass.exception.MessageException;
import ch.bbzw.jass.model.JassCard;
import ch.bbzw.jass.model.JassColor;
import ch.bbzw.jass.model.JassMatch;
import ch.bbzw.jass.model.JassMove;
import ch.bbzw.jass.model.JassRound;
import ch.bbzw.jass.model.JassUser;
import ch.bbzw.jass.model.JassValue;
import ch.bbzw.jass.model.dto.MessageDto;

public class JassRules {

	private JassRules() {
	}

	public static void checkCanPlay(JassCard card, JassRound round, JassUser user) {
		checkIsTurn(round, user);
		JassMatch match = round.getMatch();
		List<JassCard> hand = match.getHand(user);
		checkHand(card, hand);
		JassColor color = round.getColor();
		long roundColorCards = countRoundColorCards(hand, color);
		checkRoundColorRule(card, round, match, hand, color, roundColorCards);
	}

	private static void checkIsTurn(JassRound round, JassUser user) {
		if (!round.isTheTurn(user)) {
			throw new MessageException(new MessageDto("Du bist nicht an der Reihe"));
		}
	}

	private static void checkHand(JassCard card, List<JassCard> hand) {
		if (!hand.contains(card)) {
			throw new MessageException(new MessageDto("Die Karte ist nicht in deinem Besitz"));
		}
	}

	private static long countRoundColorCards(List<JassCard> hand, JassColor color) {
		return hand.stream().filter(c -> c.getColor() == color).count();
	}

	private static boolean isTrumpUnder(JassMatch match, List<JassCard> hand, JassColor color, long roundColorCards) {
		boolean isTrumpUnder = false;
		if (roundColorCards == 1 && color == match.getType().getTrumpColor()) {
			Optional<JassCard> colorCard = hand.stream().filter(c -> c.getColor() == color).findFirst();
			isTrumpUnder = colorCard.isPresent() && colorCard.get().getValue() == JassValue.UNDER;
		}
		return isTrumpUnder;
	}

	private static void checkRoundColorRule(JassCard card, JassRound round, JassMatch match, List<JassCard> hand,
			JassColor color, long roundColorCards) {
		boolean isTrumpUnder = isTrumpUnder(match, hand, color, roundColorCards);
		if (color != null && color != card.getColor() && roundColorCards > 0 && !isTrumpUnder) {
			if (match.getType().getTrumpColor() == card.getColor()) {
				checkDoNotUnderTrump(card, round, match, color);
			} else {
				throw new MessageException(new MessageDto("Du musst farben"));
			}
		}
	}

	private static void checkDoNotUnderTrump(JassCard card, JassRound round, JassMatch match, JassColor color) {
		if (color != match.getType().getTrumpColor()) {
			round.getMoves().stream().map(JassMove::getCard)
					.filter(c -> c.getColor() == match.getType().getTrumpColor()).forEach(trumpCard -> {
						if (trumpCard.getValue().getPostitionTrump() > card.getValue().getPostitionTrump()) {
							throw new MessageException(new MessageDto("Du darfst nicht untertrumpfen"));
						}
					});
		}
	}
}
