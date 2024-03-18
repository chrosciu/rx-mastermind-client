package eu.chrost.rxmastermindclient.config;

import eu.chrost.rxmastermindclient.service.InputService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InputConfig {
    @Bean
    public InputService inputService() {
        return new InputService(System.in);
    }
}
