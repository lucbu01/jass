package ch.bbzw.jass.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ch.bbzw.jass.service.UserService;

@RestController
public class IdenticationController {
	
	@Autowired
	UserService userService;

	@PostMapping("/api/name")
	public @ResponseBody UUID setName(@RequestBody String name ) {
		return userService.setName(name);
	}
}
