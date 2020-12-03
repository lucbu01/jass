package ch.bbzw.jass.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

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
}
