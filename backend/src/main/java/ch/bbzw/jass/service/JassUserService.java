package ch.bbzw.jass.service;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ch.bbzw.jass.model.JassUser;
import ch.bbzw.jass.repository.JassUserRepository;

@Service
@Transactional
public class JassUserService implements UserDetailsService {
	
	private static Logger log = LoggerFactory.getLogger(JassUserService.class);
	
	@Autowired
	JassUserRepository userRepository;

	public UUID setName(String name) {
		return userRepository.save(new JassUser(name)).getId();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			return userRepository.findById(UUID.fromString(username)).get();
		} catch (Exception e) {
			log.warn("User not found: {}", username, e);
			throw new UsernameNotFoundException("username not found");
		}
	}

	public void updateName(String name) {
		JassUser user = getUser();
		user.setName(name);
		userRepository.save(user);
	}
	
	public String getName() {
		return getUser().getName();
	}
	
	public Optional<JassUser> getLoggedInUser() {
		try {
			JassUser user = (JassUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			return Optional.ofNullable(user);
		} catch (Exception e) {
			return Optional.empty();
		}
	}
	
	public JassUser getUser() {
		return getLoggedInUser().get();
	}
	
}
