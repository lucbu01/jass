package ch.bbzw.jass.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.bbzw.jass.model.JassGame;
import ch.bbzw.jass.model.JassTeam;
import ch.bbzw.jass.repository.JassGameRepository;
import ch.bbzw.jass.repository.JassMatchRepository;
import ch.bbzw.jass.repository.JassMoveRepository;
import ch.bbzw.jass.repository.JassRoundRepository;
import ch.bbzw.jass.repository.JassTeamRepository;

@Service
public class JassGameService {

	private JassGameRepository gameRepository;
	private JassTeamRepository teamRepository;
	private JassMatchRepository matchRepository;
	private JassRoundRepository roundRepository;
	private JassMoveRepository moveRepository;
	private JassUserService userService;
	
	@Autowired
	public JassGameService(JassGameRepository gameRepository, JassTeamRepository teamRepository,
			JassMatchRepository matchRepository, JassRoundRepository roundRepository, JassMoveRepository moveRepository,
			JassUserService userService) {
		this.gameRepository = gameRepository;
		this.teamRepository = teamRepository;
		this.matchRepository = matchRepository;
		this.roundRepository = roundRepository;
		this.moveRepository = moveRepository;
		this.userService = userService;
	}

	public UUID createNewGame() {
		JassGame game = gameRepository.save(new JassGame());
		JassTeam team1 = new JassTeam(game, (byte) 0);
		JassTeam team2 = new JassTeam(game, (byte) 1);
		team1.getUsers().add(userService.getUser());
		teamRepository.save(team1);
		teamRepository.save(team2);
		return game.getId();
	}
	
	
}
