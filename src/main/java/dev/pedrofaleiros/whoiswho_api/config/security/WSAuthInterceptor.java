package dev.pedrofaleiros.whoiswho_api.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import dev.pedrofaleiros.whoiswho_api.entity.UserDetailsImpl;
import dev.pedrofaleiros.whoiswho_api.exception.NotAuthException;
import dev.pedrofaleiros.whoiswho_api.repository.UserRepository;

@Component
public class WSAuthInterceptor implements ChannelInterceptor {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        var headerAccessor = StompHeaderAccessor.wrap(message);
        var acessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            String token = headerAccessor.getFirstNativeHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                String username = tokenService.validateToken(token);
                if (username != null) {
                    var user = userRepository.findByUsername(username).orElseThrow(NotAuthException::new);
                    var userDetails = new UserDetailsImpl(user);
                    var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    acessor.setUser(auth);
                }
            }
        }

        return message;
    }

}
