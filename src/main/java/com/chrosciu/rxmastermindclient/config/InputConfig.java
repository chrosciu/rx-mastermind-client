package com.chrosciu.rxmastermindclient.config;

import com.chrosciu.rxmastermindclient.service.InputService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InputConfig {
    @Bean
    public InputService inputService() {
        return new InputService(System.in);
    }
}
