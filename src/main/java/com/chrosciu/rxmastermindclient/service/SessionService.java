package com.chrosciu.rxmastermindclient.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final WebClient webClient;

    public Mono<Long> getSessionId() {
        return null;
    }

    public Mono<String> getResult(long sessionId, String sample) {
        return null;
    }

    public Mono<Void> destroySession(long sessionId) {
        return null;
    }



}
