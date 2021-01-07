package ch.bbzw.jass.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.easymock.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class JassMatchTypeTest {

	JassTeam team1;

	JassTeam team2;

	@Mock
	JassMatch match;

	@Mock
	JassGame game;

	List<JassRound> rounds;

	@BeforeEach
	public void setUp() {
		team1 = new JassTeam(game, (byte) 0);
		JassUser user1 = new JassUser("User1", "secret");
		user1.setId(UUID.randomUUID());
		team1.getUsers().add(user1);
		JassUser user2 = new JassUser("User2", "secret");
		user2.setId(UUID.randomUUID());
		team1.getUsers().add(user2);
		team2 = new JassTeam(game, (byte) 1);
		JassUser user3 = new JassUser("User3", "secret");
		user3.setId(UUID.randomUUID());
		team2.getUsers().add(user3);
		JassUser user4 = new JassUser("User4", "secret");
		user4.setId(UUID.randomUUID());
		team2.getUsers().add(user4);

		rounds = new ArrayList<>();
		JassRound round = new JassRound(match, (short) 4);
		round.getMoves().add(new JassMove(round, (byte) 0, user1, new JassCard(JassColor.CLAMP, JassValue.UNDER)));
		round.getMoves().add(new JassMove(round, (byte) 1, user2, new JassCard(JassColor.CLAMP, JassValue.SEVEN)));
		round.getMoves().add(new JassMove(round, (byte) 2, user3, new JassCard(JassColor.ROSE, JassValue.KING)));
		round.getMoves().add(new JassMove(round, (byte) 3, user4, new JassCard(JassColor.ROSE, JassValue.NINE)));
		rounds.add(round);

		round = new JassRound(match, (short) 8);
		round.getMoves().add(new JassMove(round, (byte) 0, user1, new JassCard(JassColor.ACORN, JassValue.UNDER)));
		round.getMoves().add(new JassMove(round, (byte) 1, user2, new JassCard(JassColor.ACORN, JassValue.SEVEN)));
		round.getMoves().add(new JassMove(round, (byte) 2, user3, new JassCard(JassColor.SHIELD, JassValue.NINE)));
		round.getMoves().add(new JassMove(round, (byte) 3, user4, new JassCard(JassColor.SHIELD, JassValue.KING)));
		rounds.add(round);
	}

	@ParameterizedTest
	@CsvSource({ //
			"CLAMP,70,0", //
			"SHIELD,12,50", //
			"ROSE,44,80", //
			"ACORN,35,0", //
			"UPSIDE_DOWN,51,0", //
			"DOWNSIDE_UP,51,0", //
			"GUSTI,85,0", //
			"MERRY,85,0", //
			"SLALOM_UP,85,0", //
			"SLALOM_DOWN,85,0" //
	})
	void testCalculatePoints(String matchType, String team1, String team2) throws Exception {
		JassMatchType testee = JassMatchType.valueOf(matchType);

		assertEquals(Integer.parseInt(team1), testee.calculatePointsOfTeam(this.team1, rounds));
		assertEquals(Integer.parseInt(team2), testee.calculatePointsOfTeam(this.team2, rounds));
	}

	@ParameterizedTest
	@CsvSource({ //
			"CLAMP,User1", //
			"SHIELD,User1", //
			"ROSE,User4", //
			"ACORN,User1", //
			"UPSIDE_DOWN,User1", //
			"DOWNSIDE_UP,User2", //
			"GUSTI,User2", //
			"MERRY,User1", //
			"SLALOM_UP,User1", //
			"SLALOM_DOWN,User2" //
	})
	void testCalculateWinnerEven(String matchType, String winner) throws Exception {
		JassMatchType testee = JassMatchType.valueOf(matchType);

		JassMove move = testee.calculateWinner(rounds.get(0).getMoves(), JassColor.CLAMP, (short) 4);

		assertEquals(winner, move.getUser().getName());
	}

	@ParameterizedTest
	@CsvSource({ //
			"CLAMP,User1", //
			"SHIELD,User3", //
			"ROSE,User1", //
			"ACORN,User1", //
			"UPSIDE_DOWN,User1", //
			"DOWNSIDE_UP,User2", //
			"GUSTI,User1", //
			"MERRY,User2", //
			"SLALOM_UP,User2", //
			"SLALOM_DOWN,User1" //
	})
	void testCalculateWinnerOff(String matchType, String winner) throws Exception {
		JassMatchType testee = JassMatchType.valueOf(matchType);

		JassMove move = testee.calculateWinner(rounds.get(1).getMoves(), JassColor.ACORN, (short) 3);

		assertEquals(winner, move.getUser().getName());
	}
}
