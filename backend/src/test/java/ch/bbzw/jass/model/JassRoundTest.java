package ch.bbzw.jass.model;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JassRoundTest {

	JassGame game;
	JassTeam team1;
	JassTeam team2;
	JassUser user1;
	JassUser user2;
	JassUser user3;
	JassUser user4;
	JassMatch match;
	JassRound testee;

	@BeforeEach
	void setUp() {
		game = new JassGame();
		game.setId(UUID.randomUUID());
		team1 = new JassTeam(game, (byte) 0);
		user1 = new JassUser("User1", "secret");
		user1.setId(UUID.randomUUID());
		team1.getUsers().add(user1);
		user2 = new JassUser("User2", "secret");
		user2.setId(UUID.randomUUID());
		team1.getUsers().add(user2);
		team2 = new JassTeam(game, (byte) 1);
		user3 = new JassUser("User3", "secret");
		user3.setId(UUID.randomUUID());
		team2.getUsers().add(user3);
		user4 = new JassUser("User4", "secret");
		user4.setId(UUID.randomUUID());
		team2.getUsers().add(user4);
		game.getTeams().add(team1);
		game.getTeams().add(team2);

		match = new JassMatch(game, 0);
		match.setType(JassMatchType.UPSIDE_DOWN);
		match.setAnnouncer(user1);
		match.setPushed(true);

		game.getMatches().add(match);

		testee = new JassRound(match, (short) 0);
	}

	@Test
	void testGetTurnOf() throws Exception {
		assertEquals(user1, testee.getTurnOf());
	}

	@Test
	void testGetTurnOfWithMoves() throws Exception {
		testee.getMoves().addAll(List.of(new JassMove(), new JassMove()));
		assertEquals(user2, testee.getTurnOf());
	}

	@Test
	void testGetTurnOfDefinitiveAnnouncer() throws Exception {
		match.setType(JassMatchType.SLALOM_DOWN);
		assertEquals(user2, testee.getTurnOf());
	}

	@Test
	void testGetTurnOfNonPusht() throws Exception {
		match.setType(JassMatchType.SLALOM_DOWN);
		match.setPushed(false);
		assertEquals(user1, testee.getTurnOf());
	}

	@Test
	void testGetTurnOfNoMatchType() throws Exception {
		match.setType(null);
		assertNull(testee.getTurnOf());
	}

	@Test
	void testGetTurnOfWithLastRound() throws Exception {
		JassRound lastRound = mock(JassRound.class);
		testee.setIndex((short) 1);
		match.getRounds().clear();
		match.getRounds().addAll(List.of(lastRound, testee));
		expect(lastRound.calculateWinner()).andReturn(user3);
		replay(lastRound);

		assertEquals(testee.getTurnOf(), user3);
		verify(lastRound);
	}

	@Test
	void testGetTurnOfWithLastRoundAndPlayedMoves() throws Exception {
		JassRound lastRound = mock(JassRound.class);
		testee.setIndex((short) 1);
		match.getRounds().clear();
		match.getRounds().addAll(List.of(lastRound, testee));
		testee.getMoves().add(new JassMove());
		expect(lastRound.calculateWinner()).andReturn(user3);
		replay(lastRound);

		assertEquals(testee.getTurnOf(), user2);
		verify(lastRound);
	}

}
