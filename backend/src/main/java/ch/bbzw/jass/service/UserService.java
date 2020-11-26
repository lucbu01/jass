package ch.bbzw.jass.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.bbzw.jass.model.JassUser;
import ch.bbzw.jass.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;

	public UUID setName(String name) {
		return userRepository.save(new JassUser(name)).getId();
	}
	
	
}
