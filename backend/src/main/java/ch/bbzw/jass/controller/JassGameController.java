package ch.bbzw.jass.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import ch.bbzw.jass.exception.MessageException;
import ch.bbzw.jass.model.JassCard;
import ch.bbzw.jass.model.JassMatchType;
import ch.bbzw.jass.model.dto.GameDto;
import ch.bbzw.jass.model.dto.MessageDto;
import ch.bbzw.jass.service.JassGameService;

@Controller
public class JassGameController {
	@Autowired
	JassGameService gameService;

	@MessageMapping("/game/create")
	@SendToUser("/private/game/created")
	public UUID createGame() {
		return gameService.createNewGame();
	}

	@SubscribeMapping("/game/{id}")
	public GameDto joinGame(@DestinationVariable UUID id) {
		return gameService.joinGame(id);
	}

	@SubscribeMapping("/private/game/{id}/hand")
	public List<JassCard> getHand(@DestinationVariable UUID id) {
		return gameService.getHand(id);
	}

	@MessageMapping("/game/{id}/announce")
	public void announce(@DestinationVariable UUID id, @Payload JassMatchType type) {
		gameService.announce(id, type);
	}

	@MessageMapping("/game/{id}/push")
	public void push(@DestinationVariable UUID id) {
		gameService.push(id);
	}

	@MessageMapping("/game/{id}/play")
	public void play(@DestinationVariable UUID id, @Payload JassCard card) {
		gameService.play(id, card);
	}

	@MessageMapping("/game/{id}/start")
	public void startGame(@DestinationVariable UUID id) {
		gameService.startGame(id);
	}

	@MessageExceptionHandler
	@SendToUser("/private/messages")
	public MessageDto handleException(MessageException ex) {
		return ex.getMessageDto();
	}

	@MessageExceptionHandler
	@SendToUser("/private/messages")
	public MessageDto handleException(Throwable ex) {
		return new MessageDto(ex.getMessage());
	}
}
