package com.chrosciu.rxmastermindclient.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final WebClient webClient;

    public Mono<String> getSessionId() {
        return webClient.post()
                .uri("/session")
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getResult(String sessionId, String sample) {
        return webClient.put()
                .uri("/session/{sessionId}/{sample}", sessionId, sample)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorReturn("Bad input");
    }

    public Mono<Void> destroySession(String sessionId) {
        return webClient.delete()
                .uri("/session/{sessionId}", sessionId)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
