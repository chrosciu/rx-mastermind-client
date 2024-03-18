package eu.chrost.rxmastermindclient.config;

import eu.chrost.rxmastermindclient.service.GameService;
import eu.chrost.rxmastermindclient.service.InputService;
import eu.chrost.rxmastermindclient.service.SessionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameServiceConfig {
    @Bean
    public GameService gameService(SessionService sessionService,
                                   InputService inputService,
                       @Value("${mastermind.timeout-in-seconds:#{null}}") Long timeoutInSeconds) {
        return new GameService(sessionService, inputService, timeoutInSeconds);
    }
}
