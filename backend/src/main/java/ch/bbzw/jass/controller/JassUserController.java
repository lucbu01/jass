package ch.bbzw.jass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import ch.bbzw.jass.service.JassUserService;

@Controller
public class JassUserController {
	@Autowired
	JassUserService userService;
	
	@MessageMapping("/name")
	@SendToUser("/private/name")
	public String setUserName(@Payload String name) {
		userService.updateName( name);
		return "\"" + name + "\"";
	}
	
	@SubscribeMapping("/private/name")
	public String getUserName() {
		return "\"" + userService.getName() + "\"";
	}
}
