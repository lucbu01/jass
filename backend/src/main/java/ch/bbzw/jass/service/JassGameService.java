package ch.bbzw.jass.service;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import ch.bbzw.jass.exception.MessageException;
import ch.bbzw.jass.model.JassGame;
import ch.bbzw.jass.model.JassTeam;
import ch.bbzw.jass.model.dto.GameDto;
import ch.bbzw.jass.model.dto.MessageDto;
import ch.bbzw.jass.repository.JassGameRepository;
import ch.bbzw.jass.repository.JassMatchRepository;
import ch.bbzw.jass.repository.JassMoveRepository;
import ch.bbzw.jass.repository.JassRoundRepository;
import ch.bbzw.jass.repository.JassTeamRepository;

@Service
@Transactional
public class JassGameService {

	private SimpMessagingTemplate webSocket;
	private JassGameRepository gameRepository;
	private JassTeamRepository teamRepository;
	private JassMatchRepository matchRepository;
	private JassRoundRepository roundRepository;
	private JassMoveRepository moveRepository;
	private JassUserService userService;
	
	@Autowired
	public JassGameService(SimpMessagingTemplate webSocket, JassGameRepository gameRepository, JassTeamRepository teamRepository,
			JassMatchRepository matchRepository, JassRoundRepository roundRepository, JassMoveRepository moveRepository,
			JassUserService userService) {
		this.webSocket = webSocket;
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

	public GameDto joinGame(UUID gameId) {
		JassGame game = gameRepository.findById(gameId).orElseThrow(() -> new MessageException(new MessageDto("Spiel wurde nicht gefunden", "/")));
		JassTeam team = game.getTeams().get(0);
		if (!game.getTeams().stream().anyMatch(t -> t.getUsers().stream().anyMatch(u -> u.getId().equals(userService.getUser().getId())))) {			
			if (team.getUsers().size() > 1) {
				team = game.getTeams().get(1);
			}
			if (team.getUsers().size() > 1) {			
				throw new MessageException(new MessageDto("Das Spiel ist bereits voll", "/"));
			}
			team.getUsers().add(userService.getUser());
			teamRepository.save(team);
		}
		GameDto dto = new GameDto(game);
		update(dto);
		return dto;
	}

	public void startGame(UUID id) {
		JassGame game = get(id);
		if (game.getAllPlayers().size() == 4) {
			game.setStarted(true);
			gameRepository.save(game);
			update(new GameDto(game));
		} else {
			throw new MessageException(new MessageDto("Spiel muss 4 Spieler enthalten", "/"));
		}
	}
	
	public JassGame get(UUID id) {
		JassGame game = gameRepository.findById(id).orElseThrow(() -> new MessageException(new MessageDto("Spiel wurde nicht gefunden", "/")));
		if (game.getAllPlayers().contains(userService.getUser())) {
			return game;
		} else {
			throw new MessageException(new MessageDto("Spiel wurde nicht gefunden", "/"));
		}
	}
	
	public void update(GameDto game) {
		webSocket.convertAndSend("/public/game/" + game.getId(), game);
	}
}
