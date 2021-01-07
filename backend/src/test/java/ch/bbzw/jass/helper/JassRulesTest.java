package ch.bbzw.jass.helper;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.easymock.EasyMockExtension;
import org.easymock.Mock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import ch.bbzw.jass.exception.MessageException;
import ch.bbzw.jass.model.JassCard;
import ch.bbzw.jass.model.JassColor;
import ch.bbzw.jass.model.JassMatch;
import ch.bbzw.jass.model.JassMatchType;
import ch.bbzw.jass.model.JassMove;
import ch.bbzw.jass.model.JassRound;
import ch.bbzw.jass.model.JassUser;
import ch.bbzw.jass.model.JassValue;

@ExtendWith(EasyMockExtension.class)
class JassRulesTest {

	@Mock
	JassRound round;

	@Mock
	JassMatch match;

	JassUser user;

	List<JassCard> hand;

	@Mock
	JassMove move1;

	@Mock
	JassMove move2;

	List<JassMove> moves;

	void setUp(boolean isTurn, JassMatchType matchType, JassColor color) {
		user = new JassUser("name", "password");
		user.setId(UUID.randomUUID());
		hand = new ArrayList<>();
		hand.add(new JassCard(JassColor.ROSE, JassValue.NINE));
		hand.add(new JassCard(JassColor.ROSE, JassValue.UNDER));
		hand.add(new JassCard(JassColor.CLAMP, JassValue.UNDER));
		hand.add(new JassCard(JassColor.CLAMP, JassValue.ASS));
		hand.add(new JassCard(JassColor.SHIELD, JassValue.SEVEN));
		hand.add(new JassCard(JassColor.ACORN, JassValue.UNDER));
		moves = new ArrayList<>();
		moves.add(move1);
		moves.add(move2);
		expect(move1.getCard()).andReturn(new JassCard(color, JassValue.SIX)).anyTimes();
		expect(move2.getCard()).andReturn(
				new JassCard(matchType.getTrumpColor() != null ? matchType.getTrumpColor() : color, JassValue.TEN))
				.anyTimes();
		expect(round.isTheTurn(user)).andReturn(isTurn).anyTimes();
		expect(round.getMatch()).andReturn(match).anyTimes();
		expect(round.getColor()).andReturn(color).anyTimes();
		expect(round.getMoves()).andReturn(moves).anyTimes();
		expect(match.getType()).andReturn(matchType).anyTimes();
		expect(match.getHand(user)).andReturn(hand).anyTimes();
	}

	@Test
	void checkTrue() throws Exception {
		setUp(true, JassMatchType.UPSIDE_DOWN, JassColor.ROSE);
		JassCard card = new JassCard(JassColor.ROSE, JassValue.NINE);
		replay(round, match, move1, move2);
		JassRules.checkCanPlay(card, round, user);
		verify(round, match, move1, move2);
	}

	@Test
	void checkTurnOfFalse() throws Exception {
		setUp(false, JassMatchType.UPSIDE_DOWN, JassColor.ROSE);
		JassCard card = new JassCard(JassColor.ROSE, JassValue.NINE);
		replay(round, match, move1, move2);
		assertThrows(MessageException.class, () -> JassRules.checkCanPlay(card, round, user),
				"Du bist nicht an der Reihe");
		verify(round, match, move1, move2);
	}

	@Test
	void checkHandFalse() throws Exception {
		setUp(true, JassMatchType.UPSIDE_DOWN, JassColor.ROSE);
		JassCard card = new JassCard(JassColor.ACORN, JassValue.NINE);
		replay(round, match, move1, move2);
		assertThrows(MessageException.class, () -> JassRules.checkCanPlay(card, round, user),
				"Die Karte ist nicht in deinem Besitz");
		verify(round, match, move1, move2);
	}

	@Test
	void checkRoundColorRuleWithDownsideUp() throws Exception {
		setUp(true, JassMatchType.DOWNSIDE_UP, JassColor.ROSE);
		JassCard card = new JassCard(JassColor.ACORN, JassValue.UNDER);
		replay(round, match, move1, move2);
		assertThrows(MessageException.class, () -> JassRules.checkCanPlay(card, round, user), "Du musst farben");
		verify(round, match, move1, move2);
	}

	@Test
	void checkRoundColorRuleWithTrumpType() throws Exception {
		setUp(true, JassMatchType.ROSE, JassColor.ROSE);
		JassCard card = new JassCard(JassColor.ACORN, JassValue.UNDER);
		replay(round, match, move1, move2);
		assertThrows(MessageException.class, () -> JassRules.checkCanPlay(card, round, user), "Du musst farben");
		verify(round, match, move1, move2);
	}

	@Test
	void checkRoundColorRuleWithTrumpUnder() throws Exception {
		setUp(true, JassMatchType.ACORN, JassColor.ACORN);
		JassCard card = new JassCard(JassColor.ROSE, JassValue.UNDER);
		replay(round, match, move1, move2);
		JassRules.checkCanPlay(card, round, user);
		verify(round, match, move1, move2);
	}

	@Test
	void checkRoundColorRuleWithTrump() throws Exception {
		setUp(true, JassMatchType.CLAMP, JassColor.ROSE);
		JassCard card = new JassCard(JassColor.CLAMP, JassValue.ASS);
		replay(round, match, move1, move2);
		JassRules.checkCanPlay(card, round, user);
		verify(round, match, move1, move2);
	}

	@Test
	void checkRoundColorRuleWithOtherTrump() throws Exception {
		setUp(true, JassMatchType.ROSE, JassColor.ROSE);
		JassCard card = new JassCard(JassColor.ACORN, JassValue.UNDER);
		replay(round, match, move1, move2);
		assertThrows(MessageException.class, () -> JassRules.checkCanPlay(card, round, user), "Du musst farben");
		verify(round, match, move1, move2);
	}

	@Test
	void checkDoNotUnderTrump() throws Exception {
		setUp(true, JassMatchType.SHIELD, JassColor.ROSE);
		JassCard card = new JassCard(JassColor.SHIELD, JassValue.SEVEN);
		replay(round, match, move1, move2);
		assertThrows(MessageException.class, () -> JassRules.checkCanPlay(card, round, user),
				"Du darfst nicht untertrumpfen");
		verify(round, match, move1, move2);
	}

}
