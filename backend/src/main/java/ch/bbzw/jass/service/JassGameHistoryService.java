package ch.bbzw.jass.service;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.bbzw.jass.model.JassGame;
import ch.bbzw.jass.model.dto.history.GameHistoryDto;
import ch.bbzw.jass.repository.JassGameRepository;

@Service
@Transactional
public class JassGameHistoryService {

	private JassGameRepository gameRepository;

	@Autowired
	public JassGameHistoryService(JassGameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	public Optional<GameHistoryDto> getHistory(UUID gameId) {
		Optional<JassGame> game = gameRepository.findById(gameId);
		if (game.isPresent() && game.get().isFinished()) {
			return Optional.of(new GameHistoryDto(game.get()));
		}
		return Optional.ofNullable(null);
	}

}
