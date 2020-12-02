package ch.bbzw.jass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.bbzw.jass.repository.JassGameRepository;
import ch.bbzw.jass.repository.JassMatchRepository;
import ch.bbzw.jass.repository.JassMoveRepository;
import ch.bbzw.jass.repository.JassRoundRepository;
import ch.bbzw.jass.repository.JassTeamRepository;

@Service
public class JassGameService {

	@Autowired
	JassGameRepository gameRepository;

	@Autowired
	JassTeamRepository teamRepository;

	@Autowired
	JassMatchRepository matchRepository;

	@Autowired
	JassRoundRepository roundRepository;

	@Autowired
	JassMoveRepository moveRepository;

	@Autowired
	JassUserService userService;
}
