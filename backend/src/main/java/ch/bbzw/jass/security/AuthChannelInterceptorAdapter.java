package ch.bbzw.jass.security;




import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

		if (StompCommand.CONNECT == accessor.getCommand()) {
			List<String> authorization = accessor.getNativeHeader("user");

			if (authorization != null && !authorization.isEmpty()) {
				String secret = authorization.get(0);
				String[] authParts = secret.split(":");
				if (authParts.length > 1) {					
					Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authParts[0], authParts[1]));
					accessor.setUser(auth);
				}
			}
		}
		return message;
	}
}
