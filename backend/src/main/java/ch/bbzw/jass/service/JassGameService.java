package ch.bbzw.jass.service;

import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import ch.bbzw.jass.exception.MessageException;
import ch.bbzw.jass.helper.JassCards;
import ch.bbzw.jass.helper.JassRules;
import ch.bbzw.jass.model.JassCard;
import ch.bbzw.jass.model.JassColor;
import ch.bbzw.jass.model.JassGame;
import ch.bbzw.jass.model.JassHand;
import ch.bbzw.jass.model.JassMatch;
import ch.bbzw.jass.model.JassMatchType;
import ch.bbzw.jass.model.JassMove;
import ch.bbzw.jass.model.JassRound;
import ch.bbzw.jass.model.JassTeam;
import ch.bbzw.jass.model.JassUser;
import ch.bbzw.jass.model.JassValue;
import ch.bbzw.jass.model.dto.GameDto;
import ch.bbzw.jass.model.dto.MessageDto;
import ch.bbzw.jass.model.dto.PublicGameDto;
import ch.bbzw.jass.repository.JassGameRepository;
import ch.bbzw.jass.repository.JassHandRepository;
import ch.bbzw.jass.repository.JassMatchRepository;
import ch.bbzw.jass.repository.JassMoveRepository;
import ch.bbzw.jass.repository.JassRoundRepository;
import ch.bbzw.jass.repository.JassTeamRepository;

@Service
@Transactional
public class JassGameService {

	private static final Logger log = LoggerFactory.getLogger(JassGameService.class);

	public static final JassCard ACORN_TEN = new JassCard(JassColor.ACORN, JassValue.TEN);
	public static final String GAME_NOT_FOUND = "Spiel wurde nicht gefunden";

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

	public UUID createNewGame(boolean isPublic) {
		JassGame game = gameRepository.save(new JassGame(isPublic));
		JassTeam team1 = new JassTeam(game, (byte) 0);
		JassTeam team2 = new JassTeam(game, (byte) 1);
		team1.getUsers().add(userService.getUser());
		teamRepository.save(team1);
		teamRepository.save(team2);
		return game.getId();
	}

	public GameDto joinGame(UUID gameId) {
		JassGame game = gameRepository.findById(gameId)
				.orElseThrow(() -> new MessageException(new MessageDto(GAME_NOT_FOUND, "/")));
		JassTeam team = game.getTeams().get(0);
		if (game.getTeams().stream()
				.noneMatch(t -> t.getUsers().stream().anyMatch(u -> u.getId().equals(userService.getUser().getId())))) {
			if (team.getUsers().size() > 1) {
				team = game.getTeams().get(1);
			}
			if (team.getUsers().size() > 1) {
				throw new MessageException(new MessageDto("Das Spiel ist bereits voll", "/"));
			}
			team.getUsers().add(userService.getUser());
			teamRepository.saveAndFlush(team);
			update(gameId, new GameDto(game));
		}
		if (game.isPublicGame()) {
			updatePublic(game);
		}
		return new GameDto(game, true);
	}

	public void startGame(UUID id) {
		JassGame game = get(id);
		if (game.getAllPlayers().size() == 4) {
			game.setStarted(true);
			gameRepository.save(game);
			update(id, new GameDto(game));
			if (game.isPublicGame()) {
				updatePublic(game);
			}
			mix(game);
		} else {
			throw new MessageException(new MessageDto("Spiel muss 4 Spieler enthalten", "/"));
		}
	}

	public JassGame get(UUID id) {
		JassGame game = gameRepository.findById(id)
				.orElseThrow(() -> new MessageException(new MessageDto(GAME_NOT_FOUND, "/")));
		if (game.getAllPlayers().contains(userService.getUser())) {
			return game;
		} else {
			throw new MessageException(new MessageDto(GAME_NOT_FOUND, "/"));
		}
	}

	public List<JassCard> getHand(UUID id) {
		JassGame game = get(id);
		return game.getActualHand(userService.getUser());
	}

	private void mix(JassGame game) {
		List<JassUser> allPlayers = game.getAllPlayersSorted();
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
					message(game, new MessageDto(player.getName() + " darf ansagen"));
				}
			}
		}
		matchRepository.saveAndFlush(match);
		if (match.getIndex() > 0) {
			JassUser lastAnnouncer = game.getMatches().get(match.getIndex() - 1).getAnnouncer();
			int indexOfLastAnnouncer = allPlayers.indexOf(lastAnnouncer);
			int indexOfNewAnnouncer = indexOfLastAnnouncer == 3 ? 0 : indexOfLastAnnouncer + 1;
			JassUser newAnnouncer = allPlayers.get(indexOfNewAnnouncer);
			match.setAnnouncer(newAnnouncer);
			message(game, new MessageDto(newAnnouncer.getName() + " darf ansagen"));
		}
		update(game.getId(), new GameDto(match));
		for (JassUser player : allPlayers) {
			webSocket.convertAndSendToUser(player.getId().toString(), "/private/game/" + game.getId() + "/hand",
					match.getHand(player));
		}
	}

	public void play(UUID gameId, JassCard card) {
		JassGame game = get(gameId);
		JassMatch match = game.getMatches().get(game.getMatches().size() - 1);
		JassUser user = userService.getUser();
		JassRound round;

		if (!game.isStarted()) {
			throw new MessageException(new MessageDto("Das Spiel wurde noch nicht gestartet", "/lobby/" + gameId));
		}
		if (match.getType() == null) {
			throw new MessageException(new MessageDto("Es wurde noch nicht angesagt"));
		}
		if (game.isFinished()) {
			throw new MessageException(new MessageDto("Das Spiel wurde bereits beendet", "/history/" + gameId));
		}

		if (match.getRounds().isEmpty() || match.getRounds().get(match.getRounds().size() - 1).getMoves().size() == 4) {
			round = roundRepository.save(new JassRound(match, (short) match.getRounds().size()));
			match.getRounds().add(round);
		} else {
			round = match.getRounds().get(match.getRounds().size() - 1);
		}

		JassRules.checkCanPlay(card, round, user);
		JassHand handItem = match.getHandItems(user).stream().filter(itm -> itm.getCard().equals(card)).findFirst()
				.orElseThrow(() -> new MessageException(new MessageDto("Die Karte ist nicht in deinem Besitz")));
		handRepository.delete(handItem);
		match.getHandItems().remove(handItem);
		JassMove move = moveRepository.save(new JassMove(round, (byte) round.getMoves().size(), user, card));
		webSocket.convertAndSendToUser(user.getId().toString(), "/private/game/" + game.getId() + "/hand",
				match.getHand(user));
		round.getMoves().add(move);

		update(gameId, new GameDto(round));

		if (round.getMoves().size() == 4) {
			finishRound(round);
		} else {
			JassUser nextTurn = round.getTurnOf();
			message(nextTurn, new MessageDto("Du bist an der Reihe"));
		}
	}

	private void finishRound(JassRound round) {
		JassUser winner = round.calculateWinner();
		JassMatch match = round.getMatch();
		JassGame game = match.getGame();
		message(game, new MessageDto("Die Runde ist beendet. " + winner.getName() + " hat die Runde gewonnen."));
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			log.error("Thread sleep exception", e);
		}
		if (!game.isFinished()) {
			if (match.getRounds().size() == 9) {
				mix(game);
			} else {
				round = roundRepository.save(new JassRound(match, (short) match.getRounds().size()));
				match.getRounds().add(round);
				message(winner, new MessageDto("Du darfst anfangen"));
				update(game.getId(), new GameDto(round, true));
			}
		} else {
			message(game, new MessageDto("Das Spiel ist beendet", "/history/" + game.getId()));
			update(game.getId(), new GameDto(round, true));
		}
	}

	public void announce(UUID gameId, JassMatchType gamemode) {
		JassGame game = get(gameId);
		JassMatch match = game.getMatches().get(game.getMatches().size() - 1);
		JassUser user = userService.getUser();
		if (match.getType() == null) {
			if (user.getId().equals(match.getDefinitiveAnnouncer().getId())) {
				match.setType(gamemode);
				matchRepository.save(match);
				JassRound round = roundRepository.save(new JassRound(match, (short) match.getRounds().size()));
				match.getRounds().add(round);
				message(game, new MessageDto(round.getTurnOf().getName() + " darf beginnen"));
				update(gameId, new GameDto(match));
			} else {
				throw new MessageException(new MessageDto("Du darfst nicht ansagen"));
			}
		} else {
			throw new MessageException(new MessageDto("Der Typ wurde schon gesetzt"));
		}
	}

	public void push(UUID gameId) {
		JassGame game = get(gameId);
		JassMatch match = game.getMatches().get(game.getMatches().size() - 1);
		JassUser user = userService.getUser();
		if (user.getId().equals(match.getDefinitiveAnnouncer().getId())) {
			if (match.isPushed()) {
				update(gameId, new GameDto(match));
				throw new MessageException(new MessageDto("Es wurde schon geschoben"));
			} else {
				match.setPushed(true);
				matchRepository.save(match);
				message(game, new MessageDto(user.getName() + " hat geschoben"));
				update(gameId, new GameDto(match));
			}
		} else {
			throw new MessageException(new MessageDto("Du darfst nicht ansagen"));
		}
	}

	public List<PublicGameDto> getPublicGames() {
		return gameRepository.findByPublicGameAndStarted(true, false).stream().map(PublicGameDto::new)
				.collect(Collectors.toList());
	}

	private void update(UUID gameId, GameDto game) {
		webSocket.convertAndSend("/public/game/" + gameId, game);
	}

	private void updatePublic(JassGame updatedGame) {
		List<PublicGameDto> publicGames = getPublicGames().stream().map(pg -> {
			if (pg.getId().equals(updatedGame.getId())) {
				return new PublicGameDto(updatedGame);
			}
			return pg;
		}).collect(Collectors.toList());
		if (publicGames.stream().noneMatch(pg -> pg.getId().equals(updatedGame.getId()))) {
			publicGames.add(new PublicGameDto(updatedGame));
		}
		webSocket.convertAndSend("/public/games", publicGames);
	}

	private void message(JassGame game, MessageDto message) {
		for (JassUser player : game.getAllPlayers()) {
			message(player, message);
		}
	}

	private void message(JassUser user, MessageDto message) {
		webSocket.convertAndSendToUser(user.getId().toString(), "/private/messages", message);
	}

}
