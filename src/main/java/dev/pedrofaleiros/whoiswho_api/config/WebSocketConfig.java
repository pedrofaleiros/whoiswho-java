package dev.pedrofaleiros.whoiswho_api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import dev.pedrofaleiros.whoiswho_api.config.security.WSAuthInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${spring.rabbitmq.host}")
    private String rabbitMQHost;
    
    @Value("${spring.rabbitmq.port}")
    private int rabbitMQPort;
    
    @Value("${spring.rabbitmq.username}")
    private String rabbitMQUser;
    
    @Value("${spring.rabbitmq.password}")
    private String rabbitMQPassword;

    @Autowired
    private WSAuthInterceptor wsAuthInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // registry.enableSimpleBroker("/topic", "/queue");

        registry.enableStompBrokerRelay("/topic", "/queue")
            .setRelayHost(rabbitMQHost)
            .setRelayPort(rabbitMQPort)
            .setClientLogin(rabbitMQUser)
            .setClientPasscode(rabbitMQPassword);

        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(wsAuthInterceptor);
    }

}
