package ch.bbzw.jass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import ch.bbzw.jass.service.JassGameService;

@Controller
public class JassGameController {
	@Autowired
	JassGameService gameService;
}
