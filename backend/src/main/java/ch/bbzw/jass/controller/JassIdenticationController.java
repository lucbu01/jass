package ch.bbzw.jass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ch.bbzw.jass.service.JassUserService;

@RestController
public class JassIdenticationController {
	
	@Autowired
	JassUserService userService;

	@PostMapping("/api/name")
	public @ResponseBody String setName(@RequestBody String name ) {
		return "\"" +  userService.setName(name.replace("\"", "")) + "\"";
	}
}
