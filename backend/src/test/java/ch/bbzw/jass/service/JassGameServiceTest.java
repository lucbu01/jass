package ch.bbzw.jass.service;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.easymock.EasyMockExtension;
import org.easymock.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import ch.bbzw.jass.model.JassGame;
import ch.bbzw.jass.model.JassTeam;
import ch.bbzw.jass.model.JassUser;
import ch.bbzw.jass.repository.JassGameRepository;
import ch.bbzw.jass.repository.JassHandRepository;
import ch.bbzw.jass.repository.JassMatchRepository;
import ch.bbzw.jass.repository.JassMoveRepository;
import ch.bbzw.jass.repository.JassRoundRepository;
import ch.bbzw.jass.repository.JassTeamRepository;

@ExtendWith(EasyMockExtension.class)
class JassGameServiceTest {

	@Mock
	SimpMessagingTemplate webSocket;
	@Mock
	JassGameRepository gameRepository;
	@Mock
	JassTeamRepository teamRepository;
	@Mock
	JassMatchRepository matchRepository;
	@Mock
	JassRoundRepository roundRepository;
	@Mock
	JassMoveRepository moveRepository;
	@Mock
	JassHandRepository handRepository;
	@Mock
	JassUserService userService;

	JassGameService testee;

	@BeforeEach
	public void setUp() {
		testee = new JassGameService(webSocket, gameRepository, teamRepository, matchRepository, roundRepository,
				moveRepository, handRepository, userService);
	}

	@Test
	void createNewGameTest() {
		JassGame game = mock(JassGame.class);
		JassUser user = new JassUser("Name", "Password");
		JassTeam team1 = new JassTeam(game, (byte) 0);
		team1.getUsers().add(user);
		JassTeam team2 = new JassTeam(game, (byte) 1);
		expect(userService.getUser()).andReturn(user);
		expect(gameRepository.save(anyObject(JassGame.class))).andReturn(game);
		expect(teamRepository.save(team1)).andReturn(team1);
		expect(teamRepository.save(team2)).andReturn(team2);
		expect(game.getId()).andReturn(UUID.fromString("f4844da0-dc00-4c1b-a2b4-f8c4f1949c72"));
		replay(game, gameRepository, teamRepository, matchRepository, roundRepository, moveRepository, userService);

		UUID gameId = testee.createNewGame(false);

		verify(game, gameRepository, teamRepository, matchRepository, roundRepository, moveRepository, userService);
		assertEquals(UUID.fromString("f4844da0-dc00-4c1b-a2b4-f8c4f1949c72"), gameId);
	}

}
