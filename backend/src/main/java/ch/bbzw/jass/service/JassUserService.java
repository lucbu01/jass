package ch.bbzw.jass.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.bbzw.jass.model.JassUser;
import ch.bbzw.jass.repository.JassUserRepository;

@Service
public class JassUserService {
	
	@Autowired
	JassUserRepository userRepository;

	public UUID setName(String name) {
		return userRepository.save(new JassUser(name)).getId();
	}
	
	
}
