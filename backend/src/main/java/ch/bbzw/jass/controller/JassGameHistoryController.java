package ch.bbzw.jass.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ch.bbzw.jass.model.dto.history.GameHistoryDto;
import ch.bbzw.jass.service.JassGameHistoryService;

@RestController
public class JassGameHistoryController {

	@Autowired
	JassGameHistoryService gameHistoryService;

	@GetMapping("/api/history/{gameId}")
	public ResponseEntity<GameHistoryDto> getHistory(@PathVariable("gameId") String gameId) {
		try {
			UUID parsedGameId = UUID.fromString(gameId);
			GameHistoryDto gameHistory = gameHistoryService.getHistory(parsedGameId).orElseThrow();
			return ResponseEntity.ok(gameHistory);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
}
