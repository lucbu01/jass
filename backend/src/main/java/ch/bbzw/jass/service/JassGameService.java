package ch.bbzw.jass.service;

import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import ch.bbzw.jass.exception.MessageException;
import ch.bbzw.jass.model.JassCard;
import ch.bbzw.jass.model.JassCards;
import ch.bbzw.jass.model.JassColor;
import ch.bbzw.jass.model.JassGame;
import ch.bbzw.jass.model.JassHand;
import ch.bbzw.jass.model.JassMatch;
import ch.bbzw.jass.model.JassTeam;
import ch.bbzw.jass.model.JassUser;
import ch.bbzw.jass.model.JassValue;
import ch.bbzw.jass.model.dto.GameDto;
import ch.bbzw.jass.model.dto.MessageDto;
import ch.bbzw.jass.repository.JassGameRepository;
import ch.bbzw.jass.repository.JassHandRepository;
import ch.bbzw.jass.repository.JassMatchRepository;
import ch.bbzw.jass.repository.JassMoveRepository;
import ch.bbzw.jass.repository.JassRoundRepository;
import ch.bbzw.jass.repository.JassTeamRepository;

@Service
@Transactional
public class JassGameService {

	public static final JassCard ACORN_TEN = new JassCard(JassColor.ACORN, JassValue.TEN);

	private SimpMessagingTemplate webSocket;
	private JassGameRepository gameRepository;
	private JassTeamRepository teamRepository;
	private JassMatchRepository matchRepository;
	private JassRoundRepository roundRepository;
	private JassMoveRepository moveRepository;
	private JassHandRepository handRepository;
	private JassUserService userService;

	@Autowired
	public JassGameService(SimpMessagingTemplate webSocket, JassGameRepository gameRepository,
			JassTeamRepository teamRepository, JassMatchRepository matchRepository, JassRoundRepository roundRepository,
			JassMoveRepository moveRepository, JassHandRepository handRepository, JassUserService userService) {
		this.webSocket = webSocket;
		this.gameRepository = gameRepository;
		this.teamRepository = teamRepository;
		this.matchRepository = matchRepository;
		this.roundRepository = roundRepository;
		this.moveRepository = moveRepository;
		this.handRepository = handRepository;
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
		JassGame game = gameRepository.findById(gameId)
				.orElseThrow(() -> new MessageException(new MessageDto("Spiel wurde nicht gefunden", "/")));
		JassTeam team = game.getTeams().get(0);
		if (!game.getTeams().stream()
				.anyMatch(t -> t.getUsers().stream().anyMatch(u -> u.getId().equals(userService.getUser().getId())))) {
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
			mix(game);
		} else {
			throw new MessageException(new MessageDto("Spiel muss 4 Spieler enthalten", "/"));
		}
	}

	public JassGame get(UUID id) {
		JassGame game = gameRepository.findById(id)
				.orElseThrow(() -> new MessageException(new MessageDto("Spiel wurde nicht gefunden", "/")));
		if (game.getAllPlayers().contains(userService.getUser())) {
			return game;
		} else {
			throw new MessageException(new MessageDto("Spiel wurde nicht gefunden", "/"));
		}
	}

	public List<JassCard> getHand(UUID id) {
		JassGame game = get(id);
		return game.getActualHand(userService.getUser());
	}

	public void mix(JassGame game) {
		List<JassUser> allPlayers = game.getAllPlayers();
		List<JassCard> cards = JassCards.getJassCards();
		JassMatch match = matchRepository.save(new JassMatch(game, game.getMatches().size()));
		SecureRandom random = new SecureRandom(Long.toString(ZonedDateTime.now().toEpochSecond()).getBytes());
		for (JassUser player : allPlayers) {
			List<JassCard> hand = new ArrayList<>();
			for (int i = 0; i < 9; i++) {
				int num = random.nextInt(cards.size());
				JassCard card = cards.remove(num);
				hand.add(card);
				JassHand handItem = handRepository.saveAndFlush(new JassHand(match, player, card));
				match.getHandItems().add(handItem);
				if (match.getIndex() == 0 && card.equals(ACORN_TEN)) {
					match.setAnnouncer(player);
				}
			}
			webSocket.convertAndSendToUser(player.getId().toString(), "/private/game/" + game.getId() + "/hand",
					JassCards.sort(hand));
		}
		matchRepository.saveAndFlush(match);
		if (match.getIndex() > 0) {
			JassUser lastAnnouncer = game.getMatches().get(match.getIndex() - 1).getAnnouncer();
			int lastIndex = allPlayers.indexOf(lastAnnouncer);
			int newIndex = 0;
			if (lastIndex < 3) {
				newIndex = lastIndex + 1;
			}
			match.setAnnouncer(allPlayers.get(newIndex));
		}
		update(new GameDto(match));
	}

	public void update(GameDto game) {
		webSocket.convertAndSend("/public/game/" + game.getId(), game);
	}
}
